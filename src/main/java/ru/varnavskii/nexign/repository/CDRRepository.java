package ru.varnavskii.nexign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.varnavskii.nexign.entity.CDREntity;
import ru.varnavskii.nexign.entity.SubscriberEntity;

import java.util.List;

@Repository
public interface CDRRepository extends JpaRepository<CDREntity, Long> {
    List<CDREntity> findAllByCalling(SubscriberEntity calling);
    List<CDREntity> findAllByReceiving(SubscriberEntity receiving);
}
