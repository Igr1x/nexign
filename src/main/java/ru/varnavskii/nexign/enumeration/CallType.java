package ru.varnavskii.nexign.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallType {
    OUTGOING("01"),
    INCOMING("02");

    private final String value;
}
