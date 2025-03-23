package ru.varnavskii.nexign.util;

import java.time.LocalDateTime;

public record Range(LocalDateTime start, LocalDateTime end) {
    public boolean overlaps(Range range) {
        return !range.end().isBefore(this.start()) && !range.start().isAfter(this.end());
    }
}
