package ru.varnavskii.nexign.service;

import ru.varnavskii.nexign.entity.CDREntity;
import ru.varnavskii.nexign.entity.SubscriberEntity;

import java.util.List;

public interface CDRService {
    List<CDREntity> getAllCDRecords();

    List<CDREntity> getCDRecordsByCallingId(int id);

    List<CDREntity> getCDRecordsByReceivingId(int id);

    void saveAllRecords(List<CDREntity> records);

    void generateCDRRecords(List<SubscriberEntity> subscribers, int recordsPerSubscriber);
}
