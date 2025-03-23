package ru.varnavskii.nexign.repository.subscriber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {
    /**
     * Retrieves a subscriber by their phone number
     */
    Optional<SubscriberEntity> findByPhoneNumber(String phoneNumber);
}
