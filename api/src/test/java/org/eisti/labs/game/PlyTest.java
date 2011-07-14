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

import org.junit.Test;

import java.util.Arrays;

import static org.eisti.labs.game.Ply.Coordinate.columnIndex2Label;
import static org.eisti.labs.game.Ply.Coordinate.columnLabel2index;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author MACHIZAUD Andréa
 * @version 7/12/11
 */
public class PlyTest {

    private static final int WIDTH = 40;

    @Test
    public void testColumnTranslation() throws Exception {

        int[] columnIndex = new int[WIDTH];
        for (int i = WIDTH; i-- > 0; )
            columnIndex[i] = i;


        String[] columnLabels = new String[WIDTH];
        for (int i = WIDTH; i-- > 0; )
            columnLabels[i] = columnIndex2Label(i);


        int[] reverseColumnIndexes = new int[WIDTH];
        for (int i = WIDTH; i-- > 0; )
            reverseColumnIndexes[i] = columnLabel2index(columnLabels[i]);

        System.out.println("Previsualise column indexes : " + Arrays.toString(columnIndex));
        System.out.println("Previsualise column labels : " + Arrays.toString(columnLabels));
        System.out.println("Previsualise reverse column indexes : " + Arrays.toString(reverseColumnIndexes));


        assertArrayEquals("Translation failed",
                columnIndex,
                reverseColumnIndexes);
    }
}
