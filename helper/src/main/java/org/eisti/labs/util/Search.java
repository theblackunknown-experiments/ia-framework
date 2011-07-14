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

/**
 * @author MACHIZAUD Andréa
 * @version 7/14/11
 */
public final class Search {
    private Search() {
    }

    public static <T> boolean arrayContains(T[] array, T item) {
        for (T obj : array)
            if (obj.equals(item))
                return true;
        return false;
    }
}
