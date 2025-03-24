package ru.varnavskii.nexign.repository.cdr;

import ru.varnavskii.nexign.common.util.Range;
import ru.varnavskii.nexign.repository.cdr.entity.CDREntity;

import java.util.List;

public interface CDRJdbcRepository {
    /**
     * Saves a list of CDR entities in batch mode.
     * <p>
     * This method is designed to persist multiple CDR records efficiently,
     * reducing the number of database transactions and improving performance.
     * </p>
     *
     * @param cdrRecords the list of CDR entities to be saved
     */
    void saveAll(List<CDREntity> cdrRecords);

    /**
     * Retrieves the total duration (in seconds) of incoming calls for a specific subscriber within a given month.
     *
     * <p>
     * This method calculates the total duration of incoming calls received by a subscriber within a specified month.
     * If the {@code month} parameter is {@code null}, it will include all incoming calls, regardless of the month.
     * </p>
     *
     * @param subscriptionId The ID of the subscriber for whom the total incoming call duration is being retrieved.
     * @param month          The month (in numerical format, e.g., 1 for January, 2 for February) for which the incoming
     *                       call duration is to be calculated. If {@code null}, all months are included.
     * @return The total duration (in seconds) of incoming calls for the specified subscriber and month. If no calls
     * are found, 0 is returned.
     */
    Long findTotalIncomingCallDurationInSeconds(Long subscriptionId, Integer month);

    /**
     * Retrieves the total duration (in seconds) of outgoing calls for a specific subscriber within a given month.
     *
     * <p>
     * This method calculates the total duration of outgoing calls made by a subscriber within a specified month. If the
     * {@code month} parameter is {@code null}, it will include all outgoing calls regardless of the month.
     * </p>
     *
     * @param subscriptionId The ID of the subscriber for whom the total outgoing call duration is being retrieved.
     * @param month          The month (in numerical format, e.g., 1 for January, 2 for February) for which the outgoing call
     *                       duration is to be calculated. If {@code null}, all months are included.
     * @return The total duration (in seconds) of outgoing calls for the specified subscriber and month. If no calls
     * are found, 0 is returned.
     */
    Long findTotalOutgoingCallDurationInSeconds(Long subscriptionId, Integer month);

    /**
     * Checks whether a given call duration range overlaps with any existing CDR records
     * for a specific pair of calling and receiving phone numbers.
     *
     * <p>
     * This method verifies if the provided time range for a call intersects with any existing
     * CDR records associated with the given {@code callingPhone} and {@code receivingPhone}.
     * If an overlap is found, the method returns {@code true}, indicating that a conflict exists.
     * Otherwise, it returns {@code false}.
     * </p>
     *
     * @param callingId   The id of the caller.
     * @param receivingId The id of the recipient.
     * @param range          The time range (start and end) of the call to be checked for overlap.
     * @return {@code true} if the specified range overlaps with an existing CDR record, otherwise {@code false}.
     */
    boolean hasOverlappingCDR(long callingId, long receivingId, Range range);
}
