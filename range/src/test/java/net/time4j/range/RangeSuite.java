package net.time4j.range;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses(
    {
        AlgebraTest.class,
        BasicDateRangeTest.class,
        BasicMomentRangeTest.class,
        BasicClockRangeTest.class,
        BasicTimestampRangeTest.class,
        BoundaryTest.class,
        CalendarYearTest.class,
        ClockIntervalFormatTest.class,
        ComparatorTest.class,
        DateIntervalFormatTest.class,
        IntervalCollectionTest.class,
        MachineTimeTest.class,
        MomentIntervalFormatTest.class,
        RangeConversionTest.class,
        RangeDurationTest.class,
        RelationTest.class,
        SerializationTest.class,
        SingleUnitTest.class,
        TimestampIntervalFormatTest.class,
        YearsTest.class
    }
)
public class RangeSuite {

}