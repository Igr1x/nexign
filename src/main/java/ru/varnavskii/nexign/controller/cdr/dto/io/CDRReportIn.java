package ru.varnavskii.nexign.controller.cdr.dto.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a request for generating a CDR report.
 * <p>
 * This class is used to specify the parameters required for retrieving call records
 * for a specific subscriber within a given time period.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>{@code subscriberId} - The ID of the subscriber for whom the report is generated.</li>
 *     <li>{@code startPeriod} - The start of the reporting period.</li>
 *     <li>{@code endPeriod} - The end of the reporting period.</li>
 * </ul>
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>{@code subscriberId} must not be null.</li>
 *     <li>{@code startPeriod} must not be null.</li>
 *     <li>{@code endPeriod} must not be null.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * CDRReportIn reportRequest = new CDRReportIn(12345L,
 *     LocalDateTime.of(2025, 1, 1, 0, 0),
 *     LocalDateTime.of(2025, 1, 1, 23, 59));
 * }
 * </pre>
 */
@Getter
@AllArgsConstructor
public class CDRReportIn {
    @NotNull
    private Long subscriberId;

    @NotNull
    private LocalDateTime startPeriod;

    @NotNull
    private LocalDateTime endPeriod;
}
