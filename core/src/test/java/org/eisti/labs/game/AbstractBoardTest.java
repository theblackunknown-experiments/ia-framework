package org.eisti.labs.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;

import static org.eisti.labs.game.Ply.Coordinate.Coordinate;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

        int randomPawnID = 4;

        when(filledCase.getPawnID())
                .thenReturn(randomPawnID);
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
                randomPawnID == boardMock.getCase(4, 6).getPawnID());
        assertTrue("Pawn found where none should be",
                IBoard.ICase.NO_PAWN == boardMock.getCase(4, 4).getPawnID());
    }

    @Test
    public void neighborHoodDetection() {
        //8x8 board
        when(boardMock.getDimension())
                .thenReturn(NORMAL_GRID);

        IBoard.ICase top = mock(IBoard.ICase.class);
        IBoard.ICase bottom = mock(IBoard.ICase.class);
        IBoard.ICase left = mock(IBoard.ICase.class);
        IBoard.ICase right = mock(IBoard.ICase.class);

        IBoard.ICase center = mock(IBoard.ICase.class);
        Ply.Coordinate mid_grid = Coordinate(4, 4);

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

        IBoard.ICase[] callResult = boardMock.getCaseAround(center);
        IBoard.ICase[] expectedResult = new IBoard.ICase[]{
                bottom,
                top,
                right,
                left
        };

        assertEquals("Size differs",
                expectedResult.length,
                callResult.length);

        assertArrayEquals("Content differs",
                expectedResult,
                callResult);
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
        Ply.Coordinate mid_grid = Coordinate(4, 4);


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
