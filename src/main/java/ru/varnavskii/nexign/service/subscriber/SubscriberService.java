package ru.varnavskii.nexign.service.subscriber;

import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;

import java.util.List;

public interface SubscriberService {
    List<SubscriberEntity> getAllSubscribers();

    SubscriberEntity getSubscriberByIdOrThrowException(long id);

    SubscriberEntity getSubscriberByPhoneOrThrowException(String phone);
}
