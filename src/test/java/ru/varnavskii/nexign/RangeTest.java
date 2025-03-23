package ru.varnavskii.nexign;

import org.junit.jupiter.api.Test;

import ru.varnavskii.nexign.common.util.Range;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangeTest {

    @Test
    public void testOverlapsWhenRangesOverlap() {
        Range range1 = createRange(10, 12);
        Range range2 = createRange(11, 13);

        assertTrue(range1.overlaps(range2));
    }

    @Test
    public void testOverlapsWhenRangesDoNotOverlap() {
        Range range1 = createRange(10, 12);
        Range range2 = createRange(13, 15);

        assertFalse(range1.overlaps(range2));
    }

    @Test
    public void testOverlapsWhenRangesTouchAtTheEnd() {
        Range range1 = createRange(10, 12);
        Range range2 = createRange(12, 14);

        assertTrue(range1.overlaps(range2));
    }

    @Test
    public void testOverlapsWhenRangeIsContainedWithinAnother() {
        Range range1 = createRange(10, 14);
        Range range2 = createRange(11, 13);

        assertTrue(range1.overlaps(range2));
    }

    @Test
    public void testOverlapsWhenOneRangeIsBeforeAnother() {
        Range range1 = createRange(10, 11);
        Range range2 = createRange(12, 13);

        assertFalse(range1.overlaps(range2));
    }

    private Range createRange(int startHour, int endHour) {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, startHour, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, endHour, 0, 0);
        return new Range(start, end);
    }
}
