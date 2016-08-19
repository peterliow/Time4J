package net.time4j.range;

import net.time4j.CalendarUnit;
import net.time4j.PlainDate;
import net.time4j.format.expert.Iso8601Format;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


@RunWith(JUnit4.class)
public class BasicDateRangeTest {

    @Test
    public void isAfterOtherInterval() {
        DateInterval i1 = DateInterval.between(PlainDate.of(2014, 2, 27), PlainDate.of(2014, 5, 14));
        DateInterval i2 = DateInterval.until(PlainDate.of(2014, 2, 26));
        assertThat(
            i1.isAfter(i2),
            is(true));
        assertThat(
            i2.isBefore(i1),
            is(true));
    }

    @Test(expected=NullPointerException.class)
    public void containsNull() {
        assertThat(
            DateInterval
                .between(PlainDate.of(2014, 2, 27), PlainDate.of(2014, 5, 14))
                .contains((DateInterval) null),
            is(false));
    }

    @Test
    public void containsTemporalInside() {
        assertThat(
            DateInterval
                .between(PlainDate.of(2014, 2, 27), PlainDate.of(2014, 5, 14))
                .contains(PlainDate.of(2014, 3, 1)),
            is(true));
    }

    @Test
    public void containsTemporalLeftEdgeClosed() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval
                .between(start, end)
                .contains(start),
            is(true));
    }

    @Test
    public void containsTemporalRightEdgeClosed() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval
                .between(start, end)
                .contains(end),
            is(true));
    }

    @Test
    public void containsTemporalRightEdgeOpen() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval.between(start, end).withOpenEnd().contains(end),
            is(false));
    }

    @Test
    public void containsTemporalOutside() {
        assertThat(
            DateInterval
                .between(PlainDate.of(2014, 2, 27), PlainDate.of(2014, 5, 14))
                .contains(PlainDate.of(2012, 3, 1)),
            is(false));
    }

    @Test
    public void containsTemporalInfinitePastOutside() {
        assertThat(
            DateInterval
                .until(PlainDate.of(2014, 5, 14))
                .contains(PlainDate.of(2015, 3, 1)),
            is(false));
    }

    @Test
    public void containsTemporalInfinitePastInside() {
        assertThat(
            DateInterval
                .until(PlainDate.of(2014, 5, 14))
                .contains(PlainDate.axis().getMinimum()),
            is(true));
    }

    @Test
    public void containsTemporalInfiniteFutureOutside() {
        assertThat(
            DateInterval
                .since(PlainDate.of(2014, 5, 14))
                .contains(PlainDate.of(2012, 3, 1)),
            is(false));
    }

    @Test
    public void containsTemporalInfiniteFutureInside() {
        assertThat(
            DateInterval
                .since(PlainDate.of(2014, 5, 14))
                .contains(PlainDate.axis().getMaximum()),
            is(true));
    }

    @Test
    public void isEmptyInfinitePast() {
        assertThat(
            DateInterval
                .until(PlainDate.of(2014, 5, 14))
                .isEmpty(),
            is(false));
    }

    @Test
    public void isEmptyInfiniteFuture() {
        assertThat(
            DateInterval
                .since(PlainDate.of(2014, 5, 14))
                .isEmpty(),
            is(false));
    }

    @Test
    public void isEmptyAtomicClosed() {
        assertThat(
            DateInterval
                .atomic(PlainDate.of(2014, 5, 14))
                .isEmpty(),
            is(false));
    }

    @Test
    public void isEmptyAtomicOpen() {
        PlainDate date = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval.atomic(date).withOpenEnd().isEmpty(),
            is(true));
    }

    @Test
    public void isEmptyReverseBoundaries() throws ParseException {
        DateInterval interval =
            DateInterval.parse(
                "(2012-01-01/2012-01-01]",
                Iso8601Format.EXTENDED_CALENDAR_DATE,
                BracketPolicy.SHOW_ALWAYS);
        assertThat(interval.isEmpty(), is(true));
    }

    @Test
    public void isFiniteInfinitePast() {
        assertThat(
            DateInterval
                .until(PlainDate.of(2014, 5, 14))
                .isFinite(),
            is(false));
    }

    @Test
    public void isFiniteInfiniteFuture() {
        assertThat(
            DateInterval
                .since(PlainDate.of(2014, 5, 14))
                .isFinite(),
            is(false));
    }

    @Test
    public void isFiniteEmpty() {
        PlainDate date = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval.atomic(date).withOpenEnd().isFinite(), // empty
            is(true));
    }

    @Test
    public void isFinite() {
        assertThat(
            DateInterval.atomic(PlainDate.of(2014, 5, 14)).isFinite(),
            is(true));
    }

    @Test
    public void getStart() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval.between(start, end).getStart(),
            is(Boundary.of(IntervalEdge.CLOSED, start)));
    }

    @Test
    public void getEnd() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval.between(start, end).getEnd(),
            is(Boundary.of(IntervalEdge.CLOSED, end)));
    }

    @Test
    public void getStartAsLocalDate() {
        LocalDate start = LocalDate.parse("2014-02-27");
        LocalDate end = LocalDate.parse("2015-05-09");
        assertThat(
            DateInterval.between(start, end).getStartAsLocalDate(),
            is(start));
        assertThat(
            DateInterval.atomic(start).getStartAsLocalDate(),
            is(start));
        assertThat(
            DateInterval.until(start).getStartAsLocalDate(),
            nullValue());
    }

    @Test
    public void getEndAsLocalDate() {
        LocalDate start = LocalDate.parse("2014-02-27");
        LocalDate end = LocalDate.parse("2015-05-09");
        assertThat(
            DateInterval.between(start, end).getEndAsLocalDate(),
            is(end));
        assertThat(
            DateInterval.atomic(end).getEndAsLocalDate(),
            is(end));
        assertThat(
            DateInterval.since(start).getEndAsLocalDate(),
            nullValue());
    }

    @Test
    public void testEquals() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end1 = PlainDate.of(2014, 5, 14);
        PlainDate end2 = PlainDate.of(2014, 5, 15);
        assertThat(
            DateInterval.between(start, end1)
                .equals(DateInterval.between(start, end2)),
            is(false));
        assertThat(
            DateInterval.between(start, end1)
                .equals(DateInterval.between(start, end2).withOpenEnd()),
            is(false));
        assertThat(
            DateInterval.between(start, end1)
                .equals(DateInterval.between(start, end1)),
            is(true));
    }

    @Test
    public void testHashCode() {
        PlainDate start1 = PlainDate.of(2014, 2, 27);
        PlainDate start2 = PlainDate.of(2014, 2, 26);
        PlainDate end = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval.between(start1, end).hashCode(),
            not(DateInterval.between(start2, end).hashCode()));
        assertThat(
            DateInterval.between(start1, end).hashCode(),
            is(DateInterval.between(start1, end).hashCode()));
    }

    @Test
    public void testToStringInfinitePast() {
        assertThat(
            DateInterval
                .until(PlainDate.of(2014, 5, 14))
                .toString(),
            is("(-∞/2014-05-14]"));
    }

    @Test
    public void testToStringInfiniteFuture() {
        assertThat(
            DateInterval
                .since(PlainDate.of(2014, 5, 14))
                .toString(),
            is("[2014-05-14/+∞)"));
    }

    @Test
    public void testToStringFiniteClosed() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval
                .between(start, end)
                .toString(),
            is("[2014-02-27/2014-05-14]"));
    }

    @Test
    public void testToStringFiniteHalfOpen() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);
        assertThat(
            DateInterval.between(start, end).withOpenEnd().toString(),
            is("[2014-02-27/2014-05-14)"));
    }

    @Test
    public void withOpenEnd() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);

        DateInterval interval = DateInterval.between(start, end);
        assertThat(
            interval.withOpenEnd().getEnd().isOpen(),
            is(true));

        DateInterval infinite = DateInterval.since(start);
        assertThat(
            infinite.withOpenEnd(),
            is(infinite));
    }

    @Test
    public void withClosedEnd() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);

        DateInterval interval = DateInterval.between(start, end);
        assertThat(
            interval.withClosedEnd(),
            is(interval));
    }

    @Test
    public void withStart() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate mid = PlainDate.of(2014, 4, 20);
        PlainDate end = PlainDate.of(2014, 5, 14);

        DateInterval interval = DateInterval.between(mid, end);
        assertThat(
            interval.withStart(start),
            is(DateInterval.between(start, end)));
    }

    @Test
    public void withEnd() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate mid = PlainDate.of(2014, 4, 20);
        PlainDate end = PlainDate.of(2014, 5, 14);

        DateInterval interval = DateInterval.between(start, mid);
        assertThat(
            interval.withEnd(end),
            is(DateInterval.between(start, end)));
    }

    @Test
    public void withStartOperator() {
        PlainDate start = PlainDate.of(2014, 2, 1);
        PlainDate mid = PlainDate.of(2014, 2, 20);
        PlainDate end = PlainDate.of(2014, 5, 31);

        DateInterval interval = DateInterval.between(mid, end);
        assertThat(
            interval.withStart(PlainDate.DAY_OF_MONTH.minimized()),
            is(DateInterval.between(start, end)));
    }

    @Test(expected=IllegalStateException.class)
    public void withStartOperatorInfinite() {
        DateInterval interval = DateInterval.until(PlainDate.of(2014, 2, 20));
        interval.withStart(PlainDate.DAY_OF_MONTH.minimized());
    }

    @Test
    public void withEndOperator() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate mid = PlainDate.of(2014, 5, 20);
        PlainDate end = PlainDate.of(2014, 5, 31);

        DateInterval interval = DateInterval.between(start, mid);
        assertThat(
            interval.withEnd(PlainDate.DAY_OF_MONTH.maximized()),
            is(DateInterval.between(start, end)));
    }

    @Test(expected=IllegalStateException.class)
    public void withEndOperatorInfinite() {
        DateInterval interval = DateInterval.since(PlainDate.of(2014, 2, 20));
        interval.withEnd(PlainDate.DAY_OF_MONTH.maximized());
    }

    @Test
    public void collapseNormal() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);

        DateInterval interval = DateInterval.between(start, end);
        assertThat(
            interval.collapse().isEmpty(),
            is(true));
        assertThat(
            interval.collapse().getEnd().getTemporal().equals(start),
            is(true));
        assertThat(
            interval.collapse().getEnd().isOpen(),
            is(true));
    }

    @Test(expected=IllegalStateException.class)
    public void collapsePast() {
        PlainDate end = PlainDate.of(2014, 2, 27);
        DateInterval.until(end).collapse();
    }

    @Test
    public void move() {
        PlainDate start1 = PlainDate.of(2014, 2, 27);
        PlainDate end1 = PlainDate.of(2014, 5, 14);
        DateInterval interval = DateInterval.between(start1, end1);

        PlainDate start2 = PlainDate.of(2014, 10, 27);
        PlainDate end2 = PlainDate.of(2015, 1, 14);
        DateInterval expected = DateInterval.between(start2, end2);

        assertThat(
            interval.move(8, CalendarUnit.MONTHS),
            is(expected));
    }

    @Test
    public void streamDaily() {
        PlainDate start = PlainDate.of(2014, 2, 27);
        PlainDate end = PlainDate.of(2014, 5, 14);

        List<PlainDate> expected = new ArrayList<>();
        PlainDate date = start;
        while (!date.isAfter(end)) {
            expected.add(date);
            date = date.plus(1, CalendarUnit.DAYS);
        }

        List<PlainDate> dates;
        dates = DateInterval.between(start, end).streamDaily().parallel().collect(Collectors.toList());
        assertThat(dates, is(expected));
        dates = DateInterval.between(start, end).streamDaily().parallel().sorted().collect(Collectors.toList());
        assertThat(dates, is(expected));
        dates = DateInterval.between(start, end).streamDaily().collect(Collectors.toList());
        assertThat(dates, is(expected));
        dates = DateInterval.between(start, end).streamDaily().sorted().collect(Collectors.toList());
        assertThat(dates, is(expected));
    }

}
