package ru.varnavskii.nexign.service.cdr;

import ru.varnavskii.nexign.common.util.Range;
import ru.varnavskii.nexign.repository.cdr.entity.CDREntity;
import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;

import java.util.List;
import java.util.UUID;

public interface CDRService {
    CDREntity createCDREntity(CDREntity cdrEntity);

    List<CDREntity> getAllCDRecords();

    void saveAllRecords(List<CDREntity> records);

    void generateCDRRecords(List<SubscriberEntity> subscribers, int recordsPerSubscriber);

    List<CDREntity> getAllCDRecordsBySubscriber(SubscriberEntity subscriber);

    void generateReport(UUID reportId, SubscriberEntity subscriber, Range period);

    List<CDREntity> getAllCDRRecordsInPeriodBySubscriberId(SubscriberEntity subscriber, Range period);
}
