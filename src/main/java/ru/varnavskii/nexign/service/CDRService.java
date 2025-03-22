package ru.varnavskii.nexign.service;

import ru.varnavskii.nexign.entity.CDREntity;

import java.util.List;

public interface CDRService {
    List<CDREntity> getAllCDRecords();

    List<CDREntity> getCDRecordsByCallingId(int id);

    List<CDREntity> getCDRecordsByReceivingId(int id);
}
