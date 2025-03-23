package ru.varnavskii.nexign.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.varnavskii.nexign.dto.Call;
import ru.varnavskii.nexign.dto.UDROut;
import ru.varnavskii.nexign.repository.CDRJdbcRepository;
import ru.varnavskii.nexign.service.SubscriberService;
import ru.varnavskii.nexign.service.UDRService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UDRServiceImpl implements UDRService {

    private final SubscriberService subscriberService;
    private final CDRJdbcRepository cdrJdbcRepository;

    @Override
    @Transactional
    public UDROut getUDRReportForSubscriber(long subscriberId, Integer month) {
        var subscriber = subscriberService.getSubscriberByIdOrThrowException(subscriberId);
        long incomingTotalTime = cdrJdbcRepository.findTotalIncomingCallDurationInSeconds(subscriber.getId(), month);
        long outgoingTotalTime = cdrJdbcRepository.findTotalOutgoingCallDurationInSeconds(subscriber.getId(), month);

        String incomingFormattedTime = formatDuration(incomingTotalTime);
        String outgoingFormattedTime = formatDuration(outgoingTotalTime);
        return new UDROut(subscriber.getPhoneNumber(),
            new Call(incomingFormattedTime),
            new Call(outgoingFormattedTime));
    }

    @Override
    @Transactional
    public List<UDROut> getUDRReportForAllSubscriberByMonth(Integer month) {
        var result = new ArrayList<UDROut>();
        var allSubscribers = subscriberService.getAllSubscribers();
        for (var subscriber : allSubscribers) {
            result.add(getUDRReportForSubscriber(subscriber.getId(), month));
        }
        return result;
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}
