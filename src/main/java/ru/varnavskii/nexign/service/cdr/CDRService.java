package ru.varnavskii.nexign.service.cdr;

import ru.varnavskii.nexign.common.util.Range;
import ru.varnavskii.nexign.repository.cdr.entity.CDREntity;
import ru.varnavskii.nexign.repository.subscriber.entity.SubscriberEntity;

import java.util.List;
import java.util.UUID;

/**
 * Service to work with CDR records
 */
public interface CDRService {

    /**
     * Creates a new CDR entity.
     *
     * @param cdrEntity The CDR entity to be created.
     * @return The created CDR entity.
     */
    CDREntity createCDREntity(CDREntity cdrEntity);

    /**
     * Retrieves all CDR records.
     *
     * @return A list of all CDR records.
     */
    List<CDREntity> getAllCDRecords();

    /**
     * Saves a list of CDR records.
     *
     * @param records The list of CDR records to be saved.
     */
    void saveAllRecords(List<CDREntity> records);

    /**
     * Generates a specified number of CDR records for each subscriber.
     *
     * @param subscribers      The list of subscribers for whom the CDR records will be generated.
     * @param recordsPerSubscriber The number of CDR records to generate.
     */
    void generateCDRRecords(List<SubscriberEntity> subscribers, int recordsPerSubscriber);

    /**
     * Retrieves all CDR records for a specific subscriber.
     *
     * @param subscriber The subscriber whose CDR records are being retrieved.
     * @return A list of CDR records for the specified subscriber.
     */
    List<CDREntity> getAllCDRecordsBySubscriber(SubscriberEntity subscriber);

    /**
     * Generates a report for a specific subscriber within a given period.
     *
     * @param reportId The unique identifier of the report.
     * @param subscriber The subscriber for whom the report is being generated.
     * @param period The period for which the report is being generated.
     */
    void generateReport(UUID reportId, SubscriberEntity subscriber, Range period);

    /**
     * Retrieves all CDR records for a specific subscriber within a given period.
     *
     * @param subscriber The subscriber whose CDR records are being retrieved.
     * @param period The period for which the CDR records are being retrieved.
     * @return A list of CDR records for the specified subscriber and period.
     */
    List<CDREntity> getAllCDRRecordsInPeriodBySubscriberId(SubscriberEntity subscriber, Range period);
}
