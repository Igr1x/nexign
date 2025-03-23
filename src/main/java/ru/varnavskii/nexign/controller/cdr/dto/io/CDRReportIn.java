package ru.varnavskii.nexign.controller.cdr.dto.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

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
