package ru.varnavskii.nexign.service;

import ru.varnavskii.nexign.entity.SubscriberEntity;

import java.util.List;

public interface SubscriberService {
    List<SubscriberEntity> getAllSubscribers();

    SubscriberEntity getSubscriberByIdOrThrowException(long id);

    SubscriberEntity getSubscriberByPhoneOrThrowException(String phone);
}
