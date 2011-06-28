package org.eisti.labs.game;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Representation of a duration
 *
 * @author MACHIZAUD Andréa
 * @version 6/19/11
 */
public class Duration
        implements Cloneable, Serializable {

    private final TimeUnit unit;
    private final Long time;

    public Duration(long time, TimeUnit unit) {
        this.unit = unit;
        this.time = time;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public long getTime() {
        return time;
    }

    public Duration toNanos() {
        return unit == TimeUnit.NANOSECONDS
                ? this
                : new Duration(unit.toNanos(time), TimeUnit.NANOSECONDS);
    }

    public Duration toMicros() {
        return unit == TimeUnit.MICROSECONDS
                ? this
                : new Duration(unit.toMicros(time), TimeUnit.MICROSECONDS);
    }

    public Duration toMillis() {
        return unit == TimeUnit.MILLISECONDS
                ? this
                : new Duration(unit.toMillis(time), TimeUnit.MILLISECONDS);
    }

    public Duration toSeconds() {
        return unit == TimeUnit.SECONDS
                ? this
                : new Duration(unit.toSeconds(time), TimeUnit.SECONDS);
    }

    public Duration toMinutes() {
        return unit == TimeUnit.MINUTES
                ? this
                : new Duration(unit.toMinutes(time), TimeUnit.MINUTES);
    }

    public Duration toHours() {
        return unit == TimeUnit.HOURS
                ? this
                : new Duration(unit.toHours(time), TimeUnit.HOURS);
    }

    public Duration toDays() {
        return unit == TimeUnit.DAYS
                ? this
                : new Duration(unit.toDays(time), TimeUnit.DAYS);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(time);
        switch (unit) {
            case DAYS:
                sb.append(" day");
                break;
            case HOURS:
                sb.append(" hour");
                break;
            case MINUTES:
                sb.append(" minute");
                break;
            case SECONDS:
                sb.append(" second");
                break;
            case MICROSECONDS:
                sb.append(" microsecond");
                break;
            case MILLISECONDS:
                sb.append(" millisecond");
                break;
            case NANOSECONDS:
                sb.append(" nanosecond");
                break;
        }
        if (time > 1L)
            sb.append("s");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return 41 * (
                    41 + unit.hashCode()
            ) + time.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Duration
                && obj.hashCode() == this.hashCode();
    }

    @Override
    protected Duration clone() {
        try {
            return (Duration) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("Clone unsupported on Cloneable");
        }
    }
}
