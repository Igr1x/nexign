package ru.varnavskii.nexign.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ru.varnavskii.nexign.dto.Call;
import ru.varnavskii.nexign.dto.UDROut;
import ru.varnavskii.nexign.entity.CDREntity;
import ru.varnavskii.nexign.repository.CDRJdbcRepository;
import ru.varnavskii.nexign.service.SubscriberService;
import ru.varnavskii.nexign.service.UDRService;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UDRServiceImpl implements UDRService {

    private final SubscriberService subscriberService;
    private final CDRJdbcRepository cdrJdbcRepository;

    @Override
    public UDROut getUDRReportForSubscriber(long subscriberId, Integer month) {
        var subscriber = subscriberService.getSubscriberByIdOrThrowException(subscriberId);
        var cdrList = cdrJdbcRepository.findAllBySubscriberIdAndMonth(subscriber.getId(), month);
        long incomingTotalTime = 0;
        long outgoingTotalTime = 0;

        for (CDREntity cdr : cdrList) {
            long callDuration = Duration.between(cdr.getStartCall(), cdr.getEndCall()).getSeconds();
            if (Objects.equals(cdr.getReceiving().getId(), subscriber.getId())) {
                incomingTotalTime += callDuration;
            } else if (Objects.equals(cdr.getCalling().getId(), subscriber.getId())) {
                outgoingTotalTime += callDuration;
            }
        }
        String incomingFormattedTime = formatDuration(incomingTotalTime);
        String outgoingFormattedTime = formatDuration(outgoingTotalTime);

        return new UDROut(subscriber.getPhoneNumber(),
            new Call(incomingFormattedTime),
            new Call(outgoingFormattedTime));
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}
