package ru.varnavskii.nexign.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.varnavskii.nexign.entity.SubscriberEntity;
import ru.varnavskii.nexign.repository.SubscriberRepository;
import ru.varnavskii.nexign.service.SubscriberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;

    @Override
    public List<SubscriberEntity> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    @Override
    public SubscriberEntity getSubscriberByIdOrThrowException(long id) {
        return subscriberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscriber with id " + id + " not found"));
    }
}
