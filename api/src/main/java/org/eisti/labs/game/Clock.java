package org.eisti.labs.game;

/**
 * Duration container to represent remaining time
 *
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public class Clock {
    private long remainingTime;

    public Clock(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clock clock = (Clock) o;

        return remainingTime == clock.remainingTime;

    }

    @Override
    public int hashCode() {
        return (int) (remainingTime ^ (remainingTime >>> 32));
    }
}
