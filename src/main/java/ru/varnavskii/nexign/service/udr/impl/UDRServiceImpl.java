package ru.varnavskii.nexign.service.udr.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.varnavskii.nexign.common.util.Call;
import ru.varnavskii.nexign.controller.udr.dto.UDROut;
import ru.varnavskii.nexign.repository.cdr.CDRJdbcRepository;
import ru.varnavskii.nexign.service.subscriber.SubscriberService;
import ru.varnavskii.nexign.service.udr.UDRService;

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
        var incomingTotalTime = cdrJdbcRepository.findTotalIncomingCallDurationInSeconds(subscriber.getId(), month);
        var outgoingTotalTime = cdrJdbcRepository.findTotalOutgoingCallDurationInSeconds(subscriber.getId(), month);

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

    private String formatDuration(Long seconds) {
        if (seconds == null) {
            return "00:00:00";
        }
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}
