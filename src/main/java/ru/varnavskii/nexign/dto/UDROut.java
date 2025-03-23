package ru.varnavskii.nexign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UDROut {
    private String msisdn;
    private Call incomingCall;
    private Call outgoingCall;
}
