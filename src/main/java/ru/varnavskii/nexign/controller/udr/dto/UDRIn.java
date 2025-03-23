package ru.varnavskii.nexign.controller.udr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a request to retrieve UDR (Usage Data Record) entries for a specific subscriber and month.
 * <p>
 * This class is used to filter UDR records based on a subscriber's ID and an optional month.
 * </p>
 */
@Getter
@AllArgsConstructor
public class UDRIn {
    public static final String SUBSCRIBER_ID_NULL_MESSAGES = "Subscriber id can't be null";

    @NotNull(message = SUBSCRIBER_ID_NULL_MESSAGES)
    private Long subscriberId;

    @Nullable
    private Integer month;
}
