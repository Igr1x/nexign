package ru.varnavskii.nexign.service;

import ru.varnavskii.nexign.dto.UDROut;

public interface UDRService {
    UDROut getUDRReportForSubscriber(long subscriberId, Integer month);
}
