package ru.varnavskii.nexign.controller.cdr.dto.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Represents an CDR used for processing call data.
 * <p>
 * This class is used for validating and storing call details before they are processed
 * or persisted in the system.
 * </p>
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>{@code callType} - must not be null.</li>
 *     <li>{@code callingPhone} - must match the pattern for a Russian phone number
 *         ({@code +7XXXXXXXXXX} or {@code 8XXXXXXXXXX}).</li>
 *     <li>{@code receivingPhone} - must match the same pattern as {@code callingPhone}.</li>
 *     <li>{@code startCall} - must not be null.</li>
 *     <li>{@code endCall} - must not be null.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * CDRIn cdr = new CDRIn("01", "+79991234567", "+79876543210",
 *                        LocalDateTime.of(2025, 1, 1, 12, 0),
 *                        LocalDateTime.of(2025, 1, 1, 12, 5));
 * }
 * </pre>
 */
@Getter
@AllArgsConstructor
public class CDRIn {
    private static final String PHONE_NUMBER_PATTERN = "\"^(?:\\\\+7|8)\\\\d{10}$\"";
    private static final String INVALID_PHONE_NUMBER = "Invalid phone number";

    @NotNull
    private String callType;

    @NotNull
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = INVALID_PHONE_NUMBER)
    private String callingPhone;

    @NotNull
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = INVALID_PHONE_NUMBER)
    private String receivingPhone;

    @NotNull
    private LocalDateTime startCall;

    @NotNull
    private LocalDateTime endCall;
}
