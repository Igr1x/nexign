package ru.varnavskii.nexign.service.udr;

import ru.varnavskii.nexign.controller.udr.dto.UDROut;

import java.util.List;

/**
 * Service to work with UDR report
 */
public interface UDRService {
    /**
     * Retrieves the UDR report for a specific subscriber for a given month.
     *
     * <p>If the {@code month} parameter is {@code null}, the report will include all records, regardless of the month.</p>
     *
     * @param subscriberId The ID of the subscriber for whom the UDR report is being retrieved.
     * @param month        The month (in numerical format, e.g., 1 for January, 2 for February) for which the UDR report is to be retrieved. If {@code null}, all records will be included.
     * @return The UDR report for the specified subscriber and month.
     */
    UDROut getUDRReportForSubscriber(long subscriberId, Integer month);

    /**
     * Retrieves UDR reports for all subscribers for a given month.
     *
     * <p>If the {@code month} parameter is {@code null}, the report will include all records, regardless of the month.</p>
     *
     * @param month The month (in numerical format, e.g., 1 for January, 2 for February) for which the UDR reports are to be retrieved. If {@code null}, all records will be included.
     * @return A list of UDR reports for all subscribers in the specified month.
     */
    List<UDROut> getUDRReportForAllSubscriberByMonth(Integer month);
}
