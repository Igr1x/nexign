package ru.varnavskii.nexign.service.cdr.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.varnavskii.nexign.common.enumeration.CallType;
import ru.varnavskii.nexign.common.exception.FileException;
import ru.varnavskii.nexign.common.util.Range;
import ru.varnavskii.nexign.repository.cdr.CDRJdbcRepository;
import ru.varnavskii.nexign.repository.cdr.CDRRepository;
import ru.varnavskii.nexign.repository.cdr.entity.CDREntity;
import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;
import ru.varnavskii.nexign.service.cdr.CDRService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CDRServiceImpl implements CDRService {

    private static final String REPORTS_DIRECTORY = "reports";
    private static final String HEADERS_CDR = "callType,incoming,outgoing,startCall,endCall";

    private static final Random random = new Random();

    private final CDRRepository cdrRepository;
    private final CDRJdbcRepository cdrJdbcRepository;

    @Override
    @Transactional
    public CDREntity createCDREntity(CDREntity cdrEntity) {
        return cdrRepository.save(cdrEntity);
    }

    @Override
    public List<CDREntity> getAllCDRecords() {
        return cdrRepository.findAll();
    }

    @Override
    public void saveAllRecords(List<CDREntity> records) {
        cdrJdbcRepository.saveAll(records);
    }

    @Override
    public List<CDREntity> getAllCDRecordsBySubscriber(SubscriberEntity subscriber) {
        var receiving = cdrRepository.findAllByReceiving(subscriber);
        var calling = cdrRepository.findAllByCalling(subscriber);
        var result = new ArrayList<CDREntity>();
        result.addAll(receiving);
        result.addAll(calling);
        return result;
    }

    @Override
    public List<CDREntity> getAllCDRRecordsInPeriodBySubscriberId(SubscriberEntity subscriber, Range period) {
        return cdrRepository.findAllByPeriodAndSubscriber(period.start(), period.end(), subscriber.getId());
    }

    @Override
    public void generateReport(UUID reportId, SubscriberEntity subscriber, Range period) {
        var allCdrRecordsForSubscriber = getAllCDRRecordsInPeriodBySubscriberId(subscriber, period);
        allCdrRecordsForSubscriber.sort(Comparator.comparing(CDREntity::getStartCall));

        String fileName = subscriber.getPhoneNumber() + "_" + reportId + ".csv";
        File file = getNewReportFile(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADERS_CDR);
            writer.newLine();

            for (CDREntity cdr : allCdrRecordsForSubscriber) {
                String callType = cdr.getCallType().getValue();
                String incomingPhone = cdr.getCalling().getPhoneNumber();
                String outgoingPhone = cdr.getReceiving().getPhoneNumber();
                String startCall = cdr.getStartCall().toString();
                String endCall = cdr.getEndCall().toString();

                writer.write(String.format("%s,%s,%s,%s,%s", callType, incomingPhone, outgoingPhone, startCall, endCall));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new FileException("Error writing the report with id " + reportId + " to file", e);
        }
    }

    @Override
    public void generateCDRRecords(List<SubscriberEntity> subscribers, int callCount) {
        var callTypes = CallType.values();
        List<CDREntity> cdrRecords = new ArrayList<>();
        Map<SubscriberEntity, List<Range>> activeCalls = new HashMap<>();
        for (SubscriberEntity sub : subscribers) {
            activeCalls.put(sub, new ArrayList<>());
        }

        for (int i = 0; i < callCount; i++) {
            SubscriberEntity calling = subscribers.get(random.nextInt(subscribers.size()));
            SubscriberEntity receiving;
            do {
                receiving = subscribers.get(random.nextInt(subscribers.size()));
            } while (calling.equals(receiving));

            Range callRange = getTimeRangeForCall(calling, receiving, activeCalls);

            CDREntity cdr = createCdrRecord(calling, receiving, callRange, callTypes);
            cdrRecords.add(cdr);

            activeCalls.get(calling).add(callRange);
            activeCalls.get(receiving).add(callRange);
            activeCalls.get(calling).sort(Comparator.comparing(Range::start));
            activeCalls.get(receiving).sort(Comparator.comparing(Range::start));
        }

        this.saveAllRecords(cdrRecords);
    }

    private File getNewReportFile(String fileName) {
        File directory = new File(REPORTS_DIRECTORY);
        if (!directory.exists()) {
            var created = directory.mkdirs();
            if (!created) {
                throw new FileException("Unable to create directory " + REPORTS_DIRECTORY);
            }
        }

        return new File(directory, fileName);
    }

    private Range getTimeRangeForCall(SubscriberEntity calling,
                                      SubscriberEntity receiving,
                                      Map<SubscriberEntity, List<Range>> activeCalls) {
        List<Range> callingTimes = activeCalls.get(calling);
        List<Range> receivingTimes = activeCalls.get(receiving);

        List<Range> allCalls = new ArrayList<>();
        allCalls.addAll(callingTimes);
        allCalls.addAll(receivingTimes);

        allCalls.sort(Comparator.comparing(Range::start));

        return findRangeCall(allCalls);
    }

    private Range findRangeCall(List<Range> ranges) {
        var start = LocalDateTime.now().minusYears(1);
        var end = LocalDateTime.now().minusDays(1);
        while (true) {
            LocalDateTime availableStart = getRandomDateBetween(start, end);
            LocalDateTime availableEnd = getRandomDateBetween(availableStart, availableStart.plusHours(1));
            Range callRange = new Range(availableStart, availableEnd);

            if (!isOverlapping(callRange, ranges)) {
                return callRange;
            }
        }
    }

    private boolean isOverlapping(Range newRange, List<Range> existingRanges) {
        return existingRanges.stream()
            .anyMatch(range -> range.overlaps(newRange));
    }

    private LocalDateTime getRandomDateBetween(LocalDateTime start, LocalDateTime end) {
        long secondsBetween = ChronoUnit.SECONDS.between(start, end);
        long randomSeconds = ThreadLocalRandom.current().nextLong(secondsBetween + 1);
        return start.plusSeconds(randomSeconds);
    }

    private CDREntity createCdrRecord(SubscriberEntity calling,
                                      SubscriberEntity receiving,
                                      Range callRange,
                                      CallType[] callTypes) {
        return CDREntity.builder()
            .callType(callTypes[random.nextInt(callTypes.length)])
            .calling(calling)
            .receiving(receiving)
            .startCall(callRange.start())
            .endCall(callRange.end())
            .build();
    }
}
