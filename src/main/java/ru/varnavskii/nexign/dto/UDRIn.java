package ru.varnavskii.nexign.dto;

import lombok.Getter;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@Getter
public class UDRIn {
    @NotNull(message = "Subscriber id can't be null")
    private Long subscriberId;

    @Nullable
    private Integer month;
}
