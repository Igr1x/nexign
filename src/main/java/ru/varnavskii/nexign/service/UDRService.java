package ru.varnavskii.nexign.service;

import ru.varnavskii.nexign.dto.UDROut;

import java.util.List;

public interface UDRService {
    UDROut getUDRReportForSubscriber(long subscriberId, Integer month);

    List<UDROut> getUDRReportForAllSubscriberByMonth(Integer month);
}
