/*
 * #%L
 * Core Framework Project
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

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.eisti.labs.game.Ply.Coordinate.*;

/**
 * General template for 2D board game
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
abstract public class AbstractBoard<B extends IBoard>
        implements IBoard, Cloneable {

    /**
     * Board representation
     */
//    protected final ICase[] board;
    private final int[] board;
    /**
     * Dimension
     */
    private final Dimension boardDimension;

    private final String[] COLUMN_LABELS;
    private final String[] ROW_LABELS;

    /**
     * General constructor
     *
     * @param width  row size
     * @param height column size
     */
    protected AbstractBoard(final int width, final int height) {
        boardDimension = new Dimension(width, height);

        //INFO New column label limit is 26²
        COLUMN_LABELS = new String[width];
        for (int cursor = width; cursor-- > 0; )
            COLUMN_LABELS[cursor] = columnIndex2Label(cursor);


        ROW_LABELS = new String[height];
        for (int cursor = height; cursor-- > 0; )
            ROW_LABELS[cursor] = rowIndex2Label(cursor);

        final int boardSize = width * height;
        board = new int[boardSize];

        for (int i = boardSize; i-- > 0; )
            board[i] = NO_PAWN;

        initializeBoard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Dimension getDimension() {
        return boardDimension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getFirstRowLabel() {
        return ROW_LABELS[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getLastRowLabel() {
        return ROW_LABELS[ROW_LABELS.length - 1];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getFirstColumnLabel() {
        return COLUMN_LABELS[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getLastColumnLabel() {
        return COLUMN_LABELS[COLUMN_LABELS.length - 1];
    }

    /**
     * Internal utility function which translates characters coordinate to internal board's corresponding index
     *
     * @param column - column label
     * @param row    - row label
     * @return corresponding internal board's index
     */
    private int translate(final String column, final String row) {
        final int columnInteger = columnLabel2index(column);
        final int rowInteger = rowLabel2index(row);
        return rowInteger * getDimension().height + columnInteger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getPawn(final String column, final String row) {
        final int boardIndex = translate(column, row);
        return board[boardIndex];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getPawn(final Ply.Coordinate coordinate) {
        return getPawn(coordinate.getColumn(), coordinate.getRow());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setPawn(final String column, final String row, final int pawnID) {
        board[translate(column, row)] = pawnID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setPawn(final Ply.Coordinate coordinate, final int pawnID) {
        setPawn(coordinate.getColumn(), coordinate.getRow(), pawnID);
    }

    /**
     * Check if expected pawn is at given position
     *
     * @param coordinate - position checked
     * @param pawnID     - expected pawn
     * @return whether expected pawn is at given location or not
     */
    public final boolean isAt(final Ply.Coordinate coordinate, final int pawnID) {
        return getPawn(coordinate) == pawnID;
    }

    /**
     * Check if expected pawn is at given position
     *
     * @param column - position's column checked
     * @param row    - position's row checked
     * @param pawnID - expected pawn
     * @return whether expected pawn is at given location or not
     */
    public final boolean isAt(final String column, final String row, final int pawnID) {
        return getPawn(column, row) == pawnID;
    }

    /**
     * Fetch all neighborhood coordinates of the case at given coordinate
     *
     * @param center - coordinate of requested case
     * @return coordinates of legal cases around given one
     */
    abstract public Ply.Coordinate[] getCaseAround(final Ply.Coordinate center);

    public final Ply.Coordinate[] getFreeCaseAround(final Ply.Coordinate center) {
        final Ply.Coordinate[] neighborHood = getCaseAround(center);
        final Collection<Ply.Coordinate> emptyNeighborhood = new ArrayList<Ply.Coordinate>(neighborHood.length);
        for (Ply.Coordinate neighbor : getCaseAround(center))
            if (isAt(neighbor, NO_PAWN))
                emptyNeighborhood.add(neighbor);

        return emptyNeighborhood
                .toArray(new Ply.Coordinate[emptyNeighborhood.size()]);
    }

    /**
     * Board initialization method available for subclass
     */
    abstract protected void initializeBoard();

    @Override
    public final Iterator<Ply.Coordinate> iterator() {
        Collection<Ply.Coordinate> coordinates =
                new ArrayList<Ply.Coordinate>(getDimension().width + getDimension().height);
        for (String columnLabel : COLUMN_LABELS)
            for (String rowLabel : ROW_LABELS)
                coordinates.add(Coordinate(columnLabel, rowLabel));
        return coordinates.iterator();
    }

    @Override
    public final int hashCode() {
        return Arrays.hashCode(board);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final B clone() {
        try {
            return (B) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("Clone exception although class is cloneable");
        }
    }

    //FIXME Adapt case width to column label size
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        int gridSize = getDimension().width * 2 + 1;

        String separatorLine;
        {
            StringBuilder separatorBuilder = new StringBuilder(gridSize + 2);
            separatorBuilder.append('\n');
            separatorBuilder.append(' ');
            for (int columnCursor = gridSize; columnCursor-- > 0; )
                separatorBuilder.append('-');
            separatorBuilder.append('\n');
            separatorLine = separatorBuilder.toString();
        }

        sb.append(' ');
        for (String columnCursor : COLUMN_LABELS) {
            sb.append(' ')
                    .append(columnCursor);
        }

        //header
        sb.append(separatorLine);
        for (String rowCursor : ROW_LABELS) {
            //line - pawn
            sb.append(rowCursor)
                    .append('|');
            for (String columnCursor : COLUMN_LABELS) {
                sb.append(getPawn(columnCursor, rowCursor))
                        .append('|');
            }
            //line - separator
            sb.append(separatorLine);
        }

        return sb.toString();
    }

    @Override
    public final boolean equals(final Object that) {
        return that instanceof AbstractBoard
                && that.hashCode() == this.hashCode();
    }
}
