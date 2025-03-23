package ru.varnavskii.nexign.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the types of calls in the system
 */
@Getter
@AllArgsConstructor
public enum CallType {
    OUTGOING("01"),
    INCOMING("02");

    private final String value;
}
