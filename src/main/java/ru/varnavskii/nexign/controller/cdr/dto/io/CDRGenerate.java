package ru.varnavskii.nexign.controller.cdr.dto.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CDRGenerate {
    @NotNull(message = "Call count is required")
    @Min(value = 1, message = "Call count must be greater than 0")
    private Integer callCount;
}
