package ru.varnavskii.nexign.service.subscriber.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;
import ru.varnavskii.nexign.repository.subscriber.SubscriberRepository;
import ru.varnavskii.nexign.service.subscriber.SubscriberService;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

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

    @Override
    public SubscriberEntity getSubscriberByPhoneOrThrowException(String phone) {
        return subscriberRepository.findByPhoneNumber(phone)
            .orElseThrow(() -> new EntityNotFoundException("Subscriber with phome " + phone + " not found"));
    }

    @Override
    public SubscriberEntity createSubscriber(SubscriberEntity subscriber) {
        return subscriberRepository.save(subscriber);
    }
}
