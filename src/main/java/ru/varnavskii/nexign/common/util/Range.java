package ru.varnavskii.nexign.common.util;

import java.time.LocalDateTime;

/**
 * Represents a time range with a start and end timestamp.
 * <p>
 * This record is used to track call periods and determine whether two periods overlap.
 * </p>
 */
public record Range(LocalDateTime start, LocalDateTime end) {
    public boolean overlaps(Range range) {
        return !range.end().isBefore(this.start()) && !range.start().isAfter(this.end());
    }
}
