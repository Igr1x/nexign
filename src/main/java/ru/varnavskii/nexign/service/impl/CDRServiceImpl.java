package ru.varnavskii.nexign.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.varnavskii.nexign.entity.CDREntity;
import ru.varnavskii.nexign.repository.CDRJdbcRepository;
import ru.varnavskii.nexign.repository.CDRRepository;
import ru.varnavskii.nexign.service.CDRService;
import ru.varnavskii.nexign.service.SubscriberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CDRServiceImpl implements CDRService {

    private final CDRRepository cdrRepository;
    private final CDRJdbcRepository cdrJdbcRepository;
    private final SubscriberService subscriberService;

    @Override
    public List<CDREntity> getAllCDRecords() {
        return cdrRepository.findAll();
    }

    @Override
    public List<CDREntity> getCDRecordsByCallingId(int id) {
        var subscriber = subscriberService.getSubscriberByIdOrThrowException(id);
        return cdrRepository.findAllByCalling(subscriber);
    }

    @Override
    public List<CDREntity> getCDRecordsByReceivingId(int id) {
        var subscriber = subscriberService.getSubscriberByIdOrThrowException(id);
        return cdrRepository.findAllByReceiving(subscriber);
    }

    @Override
    public void saveAllRecords(List<CDREntity> records) {
        cdrJdbcRepository.saveAll(records);
    }
}
