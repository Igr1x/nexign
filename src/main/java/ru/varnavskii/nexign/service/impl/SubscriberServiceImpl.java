package ru.varnavskii.nexign.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ru.varnavskii.nexign.entity.SubscriberEntity;
import ru.varnavskii.nexign.repository.SubscriberRepository;
import ru.varnavskii.nexign.service.SubscriberService;

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
}
