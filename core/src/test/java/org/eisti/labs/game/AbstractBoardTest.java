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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import static org.eisti.labs.game.Ply.Coordinate.Coordinate;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractBoardTest {

    public static final Dimension NORMAL_GRID = new Dimension(8, 8);
    public static final Dimension SMALL_GRID = new Dimension(4, 4);

    @Mock
    private AbstractBoard boardMock;

    @Test
    public void expectedDimension() {
        //8x8 board
        when(boardMock.getDimension())
                .thenReturn(NORMAL_GRID);

        assertEquals("Board's width didn't match",
                (int) boardMock.getDimension().getWidth(),
                8);
        assertEquals("Board's height didn't match",
                (int) boardMock.getDimension().getHeight(),
                8);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outOfBoundsSupRequest() {
        //4x4 board
        when(boardMock.getDimension())
                .thenReturn(SMALL_GRID);

        //default behavior
        when(boardMock.getCase(anyInt(), anyInt()))
                .thenCallRealMethod();

        boardMock.getCase(2, 6);
        boardMock.getCase(6, 2);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outOfBoundsInfRequest() {
        //4x4 board
        when(boardMock.getDimension())
                .thenReturn(SMALL_GRID);

        //default behavior
        when(boardMock.getCase(anyInt(), anyInt()))
                .thenCallRealMethod();

        boardMock.getCase(-2, 0);
        boardMock.getCase(0, -2);
    }

    @Test
    public void pawnRequest() {
        IBoard.ICase filledCase = mock(IBoard.ICase.class);
        IBoard.ICase emptyArea = mock(IBoard.ICase.class);

        int arbitraryPawnID = 4;

        when(filledCase.getPawnID())
                .thenReturn(arbitraryPawnID);
        when(emptyArea.getPawnID())
                .thenReturn(IBoard.ICase.NO_PAWN);

        //4x4 board
        when(boardMock.getDimension())
                .thenReturn(NORMAL_GRID);

        when(boardMock.getCase(4, 6))
                .thenReturn(filledCase);
        when(boardMock.getCase(4, 4))
                .thenReturn(emptyArea);

        assertTrue("Bad Pawn at given coordinate",
                arbitraryPawnID == boardMock.getCase(4, 6).getPawnID());
        assertTrue("Pawn found where none should be",
                IBoard.ICase.NO_PAWN == boardMock.getCase(4, 4).getPawnID());
    }

    @Test
    public void neighborHoodDetection() {
        //8x8 board
        when(boardMock.getDimension())
                .thenReturn(NORMAL_GRID);

        final IBoard.ICase top = mock(IBoard.ICase.class);
        final IBoard.ICase bottom = mock(IBoard.ICase.class);
        final IBoard.ICase left = mock(IBoard.ICase.class);
        final IBoard.ICase right = mock(IBoard.ICase.class);

        IBoard.ICase center = mock(IBoard.ICase.class);
        Ply.Coordinate mid_grid = Coordinate('E', '5');

        when(center.getPosition())
                .thenReturn(mid_grid);

        when(boardMock.getCase(4, 5))
                .thenReturn(right);
        when(boardMock.getCase(4, 3))
                .thenReturn(left);
        when(boardMock.getCase(3, 4))
                .thenReturn(top);
        when(boardMock.getCase(5, 4))
                .thenReturn(bottom);

        when(boardMock.getCaseAround(center))
                .thenCallRealMethod();

        final IBoard.ICase[] callResult = boardMock.getCaseAround(center);
        final Collection<IBoard.ICase> expectedResult = new ArrayList<IBoard.ICase>(4) {{
            add(bottom);
            add(top);
            add(right);
            add(left);
        }};

        assertEquals("Size differs",
                expectedResult.size(),
                callResult.length);

        for (IBoard.ICase area : callResult)
            assertTrue("Content differs",
                    expectedResult.contains(area));
    }

    @Test
    public void testEmptyNeighborHood() throws Exception {
        //8x8 board
        when(boardMock.getDimension())
                .thenReturn(NORMAL_GRID);

        IBoard.ICase top = mock(IBoard.ICase.class);
        IBoard.ICase bottom = mock(IBoard.ICase.class);
        IBoard.ICase left = mock(IBoard.ICase.class);
        IBoard.ICase right = mock(IBoard.ICase.class);

        IBoard.ICase center = mock(IBoard.ICase.class);
        Ply.Coordinate mid_grid = Coordinate('E', '5');


        int randomPawnID = 4;

        when(center.getPosition())
                .thenReturn(mid_grid);

        when(boardMock.getCase(4, 5))
                .thenReturn(right);
        when(boardMock.getCase(4, 3))
                .thenReturn(left);
        when(boardMock.getCase(3, 4))
                .thenReturn(top);
        when(boardMock.getCase(5, 4))
                .thenReturn(bottom);

        when(top.getPawnID())
                .thenReturn(randomPawnID);
        when(left.getPawnID())
                .thenReturn(randomPawnID);

        when(boardMock.getCaseAround(center))
                .thenCallRealMethod();

        IBoard.ICase[] callResult = boardMock.getFreeCaseAround(center);
        IBoard.ICase[] expectedResult = new IBoard.ICase[]{
                bottom,
                right
        };

        assertEquals("Size differs",
                expectedResult.length,
                callResult.length);

        assertArrayEquals("Content differs",
                expectedResult,
                callResult);
    }
}
