package ru.varnavskii.nexign.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Getter
@Builder
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
