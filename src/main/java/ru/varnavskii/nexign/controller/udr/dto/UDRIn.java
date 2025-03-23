package ru.varnavskii.nexign.controller.udr.dto;

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
public class UDRIn {
    @NotNull(message = "Subscriber id can't be null")
    private Long subscriberId;

    @Nullable
    private Integer month;
}
