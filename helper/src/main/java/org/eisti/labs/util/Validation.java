/*
 * #%L
 * Helper Framework Project
 * %%
 * Copyright (C) 2011 L@ris's Labs
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

    public static class UnsatisfiedCheck extends RuntimeException {
        public UnsatisfiedCheck(String message) {
            super(message);
        }
    }

}
