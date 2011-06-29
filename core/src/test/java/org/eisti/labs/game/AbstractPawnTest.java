/*
 * #%L
 * Core Framework Project
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
package org.eisti.labs.game;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
@Deprecated
public class AbstractPawnTest {

    @Mock
    private AbstractPawn pawnMock = mock(AbstractPawn.class);

    @Test
    public void correctIdentifier() {
        String pawnName = "Dummy Pawn";

        when(pawnMock.getName())
                .thenReturn(pawnName);
        when(pawnMock.getIdentifier())
                .thenReturn(pawnName.hashCode());



        assertEquals("Name did not match",
                pawnName,
                pawnMock.getName());
        assertEquals("Identifier did not match",
                pawnName.hashCode(),
                pawnMock.getIdentifier());
    }

}
