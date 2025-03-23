package ru.varnavskii.nexign.repository.cdr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.varnavskii.nexign.repository.cdr.entity.CDREntity;
import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CDRRepository extends JpaRepository<CDREntity, Long> {
    List<CDREntity> findAllByCalling(SubscriberEntity calling);

    List<CDREntity> findAllByReceiving(SubscriberEntity receiving);

    @Query("SELECT c FROM CDREntity c " +
        "WHERE c.startCall >= :startPeriod " +
        "AND c.endCall <= :endPeriod " +
        "AND (c.calling.id = :subscriberId OR c.receiving.id = :subscriberId)")
    List<CDREntity> findAllByPeriodAndSubscriber(@Param("startPeriod") LocalDateTime startPeriod,
                                                 @Param("endPeriod") LocalDateTime endPeriod,
                                                 @Param("subscriberId") Long subscriberId);
}
