/*
 * #%L
 * Helper Framework Project
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
package org.eisti.labs.util;

import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
public final class Tuple<K, V> {
    private final K first;
    private final V second;

    /**
     * Factory method
     */
    public static <K, V> Tuple<K, V> Tuple(K first, V second) {
        return new Tuple<K, V>(first, second);
    }

    /**
     * Zip two collections into a Tuple collection
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Tuple<K, V>[] zip(K[] firsts, V[] seconds) {
        int length = Math.min(firsts.length, seconds.length);
        Tuple<K,V>[] results = new Tuple[length];
        for (int i = length; i-- > 0; )
            results[i] = Tuple(firsts[i], seconds[i]);
        return results;
    }

    public Tuple(K first, V second) {
        require(first != null, "First parameter is null");
        require(second != null, "Second parameter is null");
        this.first = first;
        this.second = second;
    }

    public final K getFirst() {
        return first;
    }

    public final V getSecond() {
        return second;
    }

    @Override
    public final int hashCode() {
        return 41 * (
                41 + first.hashCode()
        ) + second.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof Tuple
                && obj.hashCode() == this.hashCode();
    }
}
