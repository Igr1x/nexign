package ru.varnavskii.nexign.service.subscriber;

import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;

import java.util.List;

/**
 * Service to work with Subscribers
 */
public interface SubscriberService {

    /**
     * Retrieves all subscribers.
     */
    List<SubscriberEntity> getAllSubscribers();

    /**
     * Retrieves a subscriber by their ID. If no subscriber is found, an exception is thrown.
     *
     * @param id The ID of the subscriber to retrieve.
     * @return The subscriber entity associated with the specified ID.
     * @throws jakarta.persistence.EntityNotFoundException If no subscriber with the given ID is found.
     */
    SubscriberEntity getSubscriberByIdOrThrowException(long id);

    /**
     * Retrieves a subscriber by their phone number. If no subscriber is found, an exception is thrown.
     *
     * @param phone The phone number of the subscriber to retrieve.
     * @return The subscriber entity associated with the specified phone number.
     * @throws jakarta.persistence.EntityNotFoundException If no subscriber with the given phone number is found.
     */
    SubscriberEntity getSubscriberByPhoneOrThrowException(String phone);

    /**
     * Creates a new subscriber.
     *
     * @param subscriber The subscriber entity to be created.
     * @return The created subscriber entity.
     */
    SubscriberEntity createSubscriber(SubscriberEntity subscriber);
}
