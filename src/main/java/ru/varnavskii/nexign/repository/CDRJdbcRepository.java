package ru.varnavskii.nexign.repository;

import ru.varnavskii.nexign.entity.CDREntity;

import java.util.List;

public interface CDRJdbcRepository {
    void saveAll(List<CDREntity> cdrRecords);

    Long findTotalIncomingCallDurationInSeconds(Long subscriptionId, Integer month);

    Long findTotalOutgoingCallDurationInSeconds(Long subscriptionId, Integer month);
}
