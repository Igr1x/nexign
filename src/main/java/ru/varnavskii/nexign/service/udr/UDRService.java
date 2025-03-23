package ru.varnavskii.nexign.service.udr;

import ru.varnavskii.nexign.controller.udr.dto.UDROut;

import java.util.List;

public interface UDRService {
    UDROut getUDRReportForSubscriber(long subscriberId, Integer month);

    List<UDROut> getUDRReportForAllSubscriberByMonth(Integer month);
}
