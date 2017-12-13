package net.time4j.calendar.astro;

import net.time4j.Moment;
import net.time4j.PlainDate;
import net.time4j.PlainTime;
import net.time4j.PlainTimestamp;
import net.time4j.tz.OffsetSign;
import net.time4j.tz.Timezone;
import net.time4j.tz.ZonalOffset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(JUnit4.class)
public class MoonTest {

    @Test
    public void newMoon() {
        assertThat(
            MoonPhase.NEW_MOON.atLunation(-283),
            is(PlainTimestamp.of(1977, 2, 18, 3, 36, 53).atUTC())); // Meeus (example 49.a)
    }

    @Test
    public void moonPhaseOfLastQuarter() {
        assertThat(
            MoonPhase.LAST_QUARTER.atLunation(544),
            is(PlainTimestamp.of(2044, 1, 21, 23, 47, 7).atUTC())); // Meeus (example 49.b)
    }

    // for following tests compare with
    // http://aa.usno.navy.mil/cgi-bin/aa_phases.pl?year=2017&month=10&day=7&nump=50&format=p

    @Test
    public void newMoonBefore() {
        assertThat(
            MoonPhase.NEW_MOON.before(PlainTimestamp.of(2017, 11, 18, 11, 42).atUTC())
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 10, 19, 19, 12).atUTC()));
        assertThat(
            MoonPhase.NEW_MOON.before(PlainTimestamp.of(2017, 11, 18, 11, 43).atUTC())
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 11, 18, 11, 42).atUTC()));
    }

    @Test
    public void newMoonAfter() {
        assertThat(
            MoonPhase.NEW_MOON.after(PlainTimestamp.of(2017, 10, 19, 19, 12).atUTC()) // 19:12:06
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 10, 19, 19, 12).atUTC()));
        assertThat(
            MoonPhase.NEW_MOON.after(PlainTimestamp.of(2017, 10, 19, 19, 13).atUTC())
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 11, 18, 11, 42).atUTC()));
    }

    @Test
    public void moonPhaseOfFirstQuarterBefore() {
        assertThat(
            MoonPhase.FIRST_QUARTER.before(PlainTimestamp.of(2017, 11, 26, 17, 2).atUTC())
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 10, 27, 22, 22).atUTC()));
        assertThat(
            MoonPhase.FIRST_QUARTER.before(PlainTimestamp.of(2017, 11, 26, 17, 3).atUTC()) // 17:02:56
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 11, 26, 17, 3).atUTC()));
    }

    @Test
    public void fullMoonBefore() {
        assertThat(
            MoonPhase.FULL_MOON.before(PlainTimestamp.of(2017, 12, 3, 15, 46).atUTC())
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 11, 4, 5, 23).atUTC()));
        assertThat(
            MoonPhase.FULL_MOON.before(PlainTimestamp.of(2017, 12, 3, 15, 47).atUTC()) // 15:46:56
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 12, 3, 15, 47).atUTC()));
    }

    @Test
    public void moonPhaseOfLastQuarterBefore() {
        assertThat(
            MoonPhase.LAST_QUARTER.before(PlainTimestamp.of(2017, 11, 10, 20, 36).atUTC())
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 10, 12, 12, 25).atUTC()));
        assertThat(
            MoonPhase.LAST_QUARTER.before(PlainTimestamp.of(2017, 11, 10, 20, 37).atUTC())
                .plus(30, TimeUnit.SECONDS)
                .with(Moment.PRECISION, TimeUnit.MINUTES),
            is(PlainTimestamp.of(2017, 11, 10, 20, 37).atUTC()));
    }

    @Test
    public void illuminationOfMoon() {
        Moment m =
            JulianDay.ofEphemerisTime(
                PlainDate.of(1992, 4, 12),
                PlainTime.midnightAtStartOfDay(),
                ZonalOffset.UTC
            ).toMoment();
        assertThat(
            MoonPhase.getIllumination(m),
            is(0.68)); // Meeus (example 48.a)
        assertThat(
            MoonPhase.getIllumination(MoonPhase.NEW_MOON.after(m)),
            is(0.0));
        assertThat(
            MoonPhase.getIllumination(MoonPhase.FIRST_QUARTER.after(m)),
            is(0.5));
        assertThat(
            MoonPhase.getIllumination(MoonPhase.FULL_MOON.after(m)),
            is(1.0));
        assertThat(
            MoonPhase.getIllumination(MoonPhase.LAST_QUARTER.after(m)),
            is(0.5));
    }

    @Test
    public void moonPosition() {
        JulianDay jd =
            JulianDay.ofEphemerisTime(
                PlainDate.of(1992, 4, 12),
                PlainTime.midnightAtStartOfDay(),
                ZonalOffset.UTC
            );

        // Meeus - example 47.a
        double[] data = MoonPosition.calculateMeeus(jd.getCenturyJ2000());

        assertThat(
            data[0],
            is(0.004609595895691879)); // nutation-in-longitude
        assertThat(
            data[1],
            is(23.440635013964783)); // true obliquity in degrees
        assertThat(
            data[2],
            is(134.68846856938873)); // right ascension in degrees
        assertThat(
            data[3],
            is(13.768366716980461)); // declination in degrees
        assertThat(
            data[4],
            is(368409.6848161269)); // distance in km
    }

    @Test
    public void moonlightYannarie() {
        Timezone tz = Timezone.of("Australia/Perth");
        LunarTime lunarTime =
            LunarTime.ofLocation(tz.getID())
                .southernLatitude(22, 35, 37.31)
                .easternLongitude(114, 57, 39.24)
                .atAltitude(46)
                .build();
        LunarTime.Moonlight moonlight = lunarTime.on(PlainDate.of(2016, 7, 4));
        assertThat(moonlight.moonrise().get(),
            is(PlainTimestamp.of(2016, 7, 4, 6, 25, 10).in(tz)));
            // sea-level: 06:26:12, mooncalc: 06:26:09, noaa: 06:26
        assertThat(moonlight.moonset().get(),
            is(PlainTimestamp.of(2016, 7, 4, 17, 48, 40).in(tz)));
            // sea-level: 17:47:38, mooncalc: 17:47:45, noaa: 17:47
        assertThat(moonlight.moonriseLocal().get(),
            is(PlainTimestamp.of(2016, 7, 4, 6, 25, 10)));
        assertThat(moonlight.moonsetLocal().get(),
            is(PlainTimestamp.of(2016, 7, 4, 17, 48, 40)));
        assertThat(moonlight.moonrise(ZonalOffset.UTC).get(),
            is(PlainTimestamp.of(2016, 7, 3, 22, 25, 10)));
        assertThat(moonlight.moonset(ZonalOffset.UTC).get(),
            is(PlainTimestamp.of(2016, 7, 4, 9, 48, 40)));
        assertThat(moonlight.length(), is(41010));
        assertThat(moonlight.isAbsent(), is(false));
        assertThat(moonlight.isPresentAllDay(), is(false));
        assertThat(moonlight.isPresent(PlainTimestamp.of(2016, 7, 4, 6, 25, 9).in(tz)), is(false));
        assertThat(moonlight.isPresent(PlainTimestamp.of(2016, 7, 4, 6, 25, 10).in(tz)), is(true));
    }

    @Test
    public void moonlightLondon() {
        Timezone tz = Timezone.of("Europe/London");
        LunarTime lunarTime =
            LunarTime.ofLocation(tz.getID())
                .northernLatitude(51, 30, 33.8)
                .westernLongitude(0, 7, 5.95)
                .build();
        LunarTime.Moonlight moonlight = lunarTime.on(PlainDate.of(2016, 8, 19));
        assertThat(moonlight.moonrise().get(),
            is(PlainTimestamp.of(2016, 8, 19, 20, 45, 13).in(tz)));
        assertThat(moonlight.moonset().get(),
            is(PlainTimestamp.of(2016, 8, 19, 7, 3, 14).in(tz)));
        assertThat(moonlight.length(), is(37081));
        assertThat(moonlight.isAbsent(), is(false));
        assertThat(moonlight.isPresentAllDay(), is(false));
    }

    @Test
    public void moonlightMunich() {
        Timezone tz = Timezone.of("Europe/Berlin");
        LunarTime lunarTime = LunarTime.ofLocation(tz.getID(), 48.1, 11.6);
        LunarTime.Moonlight moonlight = lunarTime.on(PlainDate.of(2000, 3, 25));
        assertThat(moonlight.moonrise().isPresent(), is(false));
        assertThat(moonlight.moonset().get(),
            is(PlainTimestamp.of(2000, 3, 25, 8, 58, 33).in(tz)));
        assertThat(moonlight.length(), is(32313));
        assertThat(moonlight.isAbsent(), is(false));
        assertThat(moonlight.isPresentAllDay(), is(false));
    }

    @Test
    public void moonlightShanghai() {
        Timezone tz = Timezone.of("Asia/Shanghai");
        LunarTime lunarTime =
            LunarTime.ofLocation(tz.getID())
                .northernLatitude(31, 14, 0.0)
                .easternLongitude(121, 28, 0.0)
                .build();
        LunarTime.Moonlight moonlight = lunarTime.on(PlainDate.of(2017, 12, 13));
        assertThat(moonlight.moonrise().get(),
            is(PlainTimestamp.of(2017, 12, 13, 1, 55, 53).in(tz)));
        assertThat(moonlight.moonset().get(),
            is(PlainTimestamp.of(2017, 12, 13, 13, 54, 32).in(tz)));
        assertThat(moonlight.length(), is(43119));
        assertThat(moonlight.isAbsent(), is(false));
        assertThat(moonlight.isPresentAllDay(), is(false));
    }

    @Test
    public void moonlightPolarCircle() { // see also: https://www.mooncalc.org
        ZonalOffset offset = ZonalOffset.ofHours(OffsetSign.AHEAD_OF_UTC, 2);
        LunarTime lunarTime = LunarTime.ofLocation(offset, 65, 10);

        LunarTime.Moonlight moonlight = lunarTime.on(PlainDate.of(2007, 6, 14));
        assertThat(moonlight.moonrise().isPresent(), is(false));
        assertThat(moonlight.moonset().isPresent(), is(false));
        assertThat(moonlight.length(), is(86400));
        assertThat(moonlight.isAbsent(), is(false));
        assertThat(moonlight.isPresentAllDay(), is(true));

        LunarTime.Moonlight moonlight2 = lunarTime.on(PlainDate.of(2007, 6, 30));
        assertThat(moonlight2.moonrise().isPresent(), is(false));
        assertThat(moonlight2.moonset().isPresent(), is(false));
        assertThat(moonlight2.length(), is(0));
        assertThat(moonlight2.isAbsent(), is(true));
        assertThat(moonlight2.isPresentAllDay(), is(false));
    }

}