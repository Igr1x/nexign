package ru.varnavskii.nexign.controller.udr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import ru.varnavskii.nexign.common.Call;

@Data
@AllArgsConstructor
public class UDROut {
    private String msisdn;
    private Call incomingCall;
    private Call outgoingCall;
}
