package org.eisti.labs.game;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;

/**
 * @author MACHIZAUD Andréa
 * @version 6/19/11
 */
public class DurationTest {

    /*
       1 day * 24 hours
        * 60 minutes * 60 seconds
        * 100 microseconds
        * 10 milliseconds
        * 1000 nanoseconds
    */
    long duration = 1L * 24L * 60L * 60L * 1000L * 1000L * 1000L;

    public static final int DAY = 0;
    public static final int HOUR = 1;
    public static final int MINUTE = 2;
    public static final int SECOND = 3;
    public static final int MILLISECOND = 4;
    public static final int MICROSECOND = 5;
    public static final int NANOSECOND = 6;

    private  static Duration[] units;

    @BeforeClass
    public static void initialDuration() {
        units = new Duration[7];
        units[DAY] = new Duration(1L, TimeUnit.DAYS);
        units[HOUR] = units[DAY].toHours();
        units[MINUTE] = units[HOUR].toMinutes();
        units[SECOND] = units[MINUTE].toSeconds();
        units[MILLISECOND] = units[SECOND].toMillis();
        units[MICROSECOND] = units[MILLISECOND].toMicros();
        units[NANOSECOND] = units[MICROSECOND].toNanos();
    }

    @AfterClass
    public static void freeArray() {
        units = null;
    }

    @Test
    public void testDay() throws Exception {
        assertEquals(units[DAY].toNanos().getTime(), duration);
        assertEquals(units[DAY].toString(), "1 day");
    }

    @Test
    public void testHour() throws Exception {
        assertEquals(units[HOUR].toNanos().getTime(), duration);
        assertEquals(units[HOUR].toString(), "24 hours");
    }

    @Test
    public void testMinute() throws Exception {
        assertEquals(units[MINUTE].toNanos().getTime(), duration);
        assertEquals(units[MINUTE].toString(), "1440 minutes");
    }

    @Test
    public void testSecond() throws Exception {
        assertEquals(units[SECOND].toNanos().getTime(), duration);
        assertEquals(units[SECOND].toString(), "86400 seconds");
    }

    @Test
    public void testMillisecond() throws Exception {
        assertEquals(units[MILLISECOND].toNanos().getTime(), duration);
        assertEquals(units[MILLISECOND].toString(), "86400000 milliseconds");
    }

    @Test
    public void testMicrosecond() throws Exception {
        assertEquals(units[MICROSECOND].toNanos().getTime(), duration);
        assertEquals(units[MICROSECOND].toString(), "86400000000 microseconds");
    }

    @Test
    public void testNanosecond() throws Exception {
        assertEquals(units[NANOSECOND].toNanos().getTime(), duration);
        assertEquals(units[NANOSECOND].toString(), "86400000000000 nanoseconds");
    }
}
