/*
 * #%L
 * API Interface Project
 * %%
 * Copyright (C) 2011 MACHIZAUD Andréa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.eisti.labs.game;

import java.util.concurrent.TimeUnit;

/**
 * Duration container to represent remaining time
 *
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public final class Clock {
    private long time;

    public Clock(long time) {
        this(time, TimeUnit.MILLISECONDS);
    }

    public Clock(long time, TimeUnit unit) {
        this.time = unit.toMillis(time);
    }

    public final long getTime() {
        return time;
    }

    public final void setTime(long time) {
        this.time = time;
    }

    @Override
    public final String toString() {
        return Double.toString(time / 1000.0) + "s";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clock clock = (Clock) o;

        return time == clock.time;

    }

    @Override
    public final int hashCode() {
        return (int) (time ^ (time >>> 32));
    }
}
