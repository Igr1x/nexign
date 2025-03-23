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
    /**
     * Finds all incoming calls made by a specific subscriber.
     */
    List<CDREntity> findAllByCalling(SubscriberEntity calling);

    /**
     * Finds all outgoing calls made by a specific subscriber.
     */
    List<CDREntity> findAllByReceiving(SubscriberEntity receiving);

    /**
     * Finds all call records for a specific subscriber within a given period.
     *
     * <p>
     * This method retrieves a list of {@link CDREntity} objects representing calls where the given subscriber is either
     * the caller or the receiver, and the call falls within the specified time period.
     * </p>
     *
     * <p>
     * Only calls that start on or after of {@code startPeriod}
     * AND end on or before of {@code endPeriond} will be included.
     * </p>
     *
     * @param startPeriod  and The start of the period for which the calls are being retrieved.
     * @param endPeriod    The end of the period for which the calls are being retrieved.
     * @param subscriberId The ID of the subscriber whose calls (either made or received) are being retrieved.
     * @return A list of {@link CDREntity} objects representing calls made or received by the specified subscriber within
     * the given time period. If no calls are found, an empty list is returned.
     */
    @Query("SELECT c FROM CDREntity c " +
        "WHERE c.startCall >= :startPeriod " +
        "AND c.endCall <= :endPeriod " +
        "AND (c.calling.id = :subscriberId OR c.receiving.id = :subscriberId)")
    List<CDREntity> findAllByPeriodAndSubscriber(@Param("startPeriod") LocalDateTime startPeriod,
                                                 @Param("endPeriod") LocalDateTime endPeriod,
                                                 @Param("subscriberId") Long subscriberId);

}
