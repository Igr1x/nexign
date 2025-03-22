package ru.varnavskii.nexign.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.availability.ApplicationAvailabilityBean;
import org.springframework.stereotype.Service;

import ru.varnavskii.nexign.entity.CDREntity;
import ru.varnavskii.nexign.entity.SubscriberEntity;
import ru.varnavskii.nexign.enumeration.CallType;
import ru.varnavskii.nexign.repository.CDRJdbcRepository;
import ru.varnavskii.nexign.repository.CDRRepository;
import ru.varnavskii.nexign.service.CDRService;
import ru.varnavskii.nexign.service.SubscriberService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CDRServiceImpl implements CDRService {

    private static final int ONE_MINUTE_IN_SEC = 3600;

    private static final Random random = new Random();

    private final CDRRepository cdrRepository;
    private final CDRJdbcRepository cdrJdbcRepository;
    private final SubscriberService subscriberService;
    private final ApplicationAvailabilityBean applicationAvailability;

    @Override
    public List<CDREntity> getAllCDRecords() {
        return cdrRepository.findAll();
    }

    @Override
    public List<CDREntity> getCDRecordsByCallingId(int id) {
        var subscriber = subscriberService.getSubscriberByIdOrThrowException(id);
        return cdrRepository.findAllByCalling(subscriber);
    }

    @Override
    public List<CDREntity> getCDRecordsByReceivingId(int id) {
        var subscriber = subscriberService.getSubscriberByIdOrThrowException(id);
        return cdrRepository.findAllByReceiving(subscriber);
    }

    @Override
    public void saveAllRecords(List<CDREntity> records) {
        cdrJdbcRepository.saveAll(records);
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

            CDREntity cdr = CDREntity.builder()
                .callType(callTypes[random.nextInt(callTypes.length)])
                .calling(calling)
                .receiving(receiving)
                .startCall(callRange.start())
                .endCall(callRange.end())
                .build();
            cdrRecords.add(cdr);

            activeCalls.get(calling).add(callRange);
            activeCalls.get(receiving).add(callRange);

            activeCalls.get(calling).sort(Comparator.comparing(Range::start));
            activeCalls.get(receiving).sort(Comparator.comparing(Range::start));
        }

        cdrJdbcRepository.saveAll(cdrRecords);
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

        return getRangeCall(allCalls);
    }

    private Range getRangeCall(List<Range> ranges) {
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

    private record Range(LocalDateTime start, LocalDateTime end) {
        public boolean overlaps(Range range) {
            return !range.end().isBefore(this.start()) && !range.start().isAfter(this.end());
        }
    }
}
