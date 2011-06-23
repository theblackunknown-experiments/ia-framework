package org.eisti.labs.util;

/**
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public class Validation {
    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Validation.class.getCanonicalName());

    /**
     * Runtime assert-like
     *
     * @param predicate predicate to verified
     * @param message   error message if not satisfied
     */
    public static void require(boolean predicate, String message) {
        if (!predicate)
            throw new UnsatisfiedCheck("Predicate not satisfied : " + message);
    }

    public static boolean nullPresent(Object[] items) {
        for (int i = items.length; i-- > 0; )
            if (items[i] == null)
                return true;
        return false;
    }

    static class UnsatisfiedCheck extends RuntimeException {
        public UnsatisfiedCheck(String message) {
            super(message);
        }
    }

}
