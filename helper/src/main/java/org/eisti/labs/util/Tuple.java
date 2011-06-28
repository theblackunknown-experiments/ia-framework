package org.eisti.labs.util;

import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
public class Tuple<K, V> {
    private K first;
    private V second;

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

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return 41 * (
                41 + first.hashCode()
        ) + second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tuple
                && obj.hashCode() == this.hashCode();
    }
}
