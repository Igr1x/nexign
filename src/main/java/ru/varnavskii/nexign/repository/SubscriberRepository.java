package ru.varnavskii.nexign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.varnavskii.nexign.entity.SubscriberEntity;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {
    Optional<SubscriberEntity> findByPhoneNumber(String phoneNumber);
}
