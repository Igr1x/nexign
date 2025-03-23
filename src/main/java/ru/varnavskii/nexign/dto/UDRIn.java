package ru.varnavskii.nexign.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class UDRIn {
    @NotNull(message = "Subscriber id can't be null")
    public Long subscriberId;

    @Nullable
    public Integer month;
}
