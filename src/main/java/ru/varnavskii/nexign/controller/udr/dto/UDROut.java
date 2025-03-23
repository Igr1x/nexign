package ru.varnavskii.nexign.controller.udr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.varnavskii.nexign.common.util.Call;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UDROut {
    private String msisdn;
    private Call incomingCall;
    private Call outgoingCall;
}
