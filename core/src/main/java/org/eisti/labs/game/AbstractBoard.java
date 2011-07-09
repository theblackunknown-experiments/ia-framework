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

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.eisti.labs.game.Ply.Coordinate.Coordinate;

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
    protected final int[] board;
    /**
     * Dimension
     */
    private final Dimension boardDimension;

    private char[] COLUMN_LABELS;
    private char[] ROW_LABELS;

    /**
     * General constructor
     *
     * @param width  row size
     * @param height column size
     */
    protected AbstractBoard(int width, int height) {
        boardDimension = new Dimension(width, height);

        //FIXME width or height > 26
        COLUMN_LABELS = new char[width];
        for (char i = 0; i < width; i++)
            COLUMN_LABELS[i] = (char) ('A' + i);

        ROW_LABELS = new char[height];
        for (char i = 0; i < height; i++)
            ROW_LABELS[i] = (char) ('1' + i);

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
    public final char getFirstRowLabel() {
        return ROW_LABELS[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final char getLastRowLabel() {
        return ROW_LABELS[ROW_LABELS.length - 1];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final char getFirstColumnLabel() {
        return COLUMN_LABELS[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final char getLastColumnLabel() {
        return COLUMN_LABELS[COLUMN_LABELS.length - 1];
    }

    /**
     * Internal utility function which translates characters coordinate to internal board's corresponding index
     *
     * @param column - column label
     * @param row    - row label
     * @return corresponding internal board's index
     */
    private int translate(final char column, final char row) {
        final int columnInteger = column - getFirstColumnLabel();
        final int rowInteger = row - getFirstRowLabel();
        return rowInteger * getDimension().height + columnInteger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getPawn(char column, char row) {
        final int boardIndex = translate(column, row);
        return board[boardIndex];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getPawn(Ply.Coordinate coordinate) {
        return getPawn(coordinate.getColumn(), coordinate.getRow());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setPawn(char column, char row, int pawnID) {
        board[translate(column, row)] = pawnID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setPawn(Ply.Coordinate coordinate, int pawnID) {
        setPawn(coordinate.getColumn(), coordinate.getRow(), pawnID);
    }

    /**
     * Check if expected pawn is at given position
     *
     * @param coordinate - position checked
     * @param pawnID     - expected pawn
     * @return whether expected pawn is at given location or not
     */
    public final boolean isAt(Ply.Coordinate coordinate, int pawnID) {
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
    public final boolean isAt(char column, char row, int pawnID) {
        return getPawn(column, row) == pawnID;
    }

    public final Ply.Coordinate[] getCaseAround(Ply.Coordinate center) {
        Collection<Ply.Coordinate> neighborhood = new ArrayList<Ply.Coordinate>(4);

        //vertical neighbor
        char centerRow = center.getRow();
        if (centerRow == getFirstRowLabel()) {
            neighborhood.add(
                    Coordinate(
                            (char) (center.getRow() + 1),
                            (char) center.getColumn()));
        } else if (centerRow == getLastRowLabel()) {
            neighborhood.add(
                    Coordinate(
                            (char) (center.getRow() - 1),
                            (char) center.getColumn()));
        } else {
            neighborhood.add(
                    Coordinate(
                            (char) (center.getRow() + 1),
                            (char) center.getColumn()));
            neighborhood.add(
                    Coordinate(
                            (char) (center.getRow() - 1),
                            (char) center.getColumn()));
        }

        char centerColumn = center.getColumn();
        if (centerColumn == getFirstColumnLabel()) {
            neighborhood.add(
                    Coordinate(
                            (char) center.getRow(),
                            (char) (center.getColumn() + 1)));
        } else if (centerColumn == getLastColumnLabel()) {
            neighborhood.add(
                    Coordinate(
                            (char) center.getRow(),
                            (char) (center.getColumn() - 1)));
        } else {
            neighborhood.add(
                    Coordinate(
                            (char) center.getRow(),
                            (char) (center.getColumn() + 1)));
            neighborhood.add(
                    Coordinate(
                            (char) center.getRow(),
                            (char) (center.getColumn() - 1)));
        }

        return neighborhood
                .toArray(new Ply.Coordinate[neighborhood.size()]);
    }

    public final Ply.Coordinate[] getFreeCaseAround(Ply.Coordinate center) {
        Collection<Ply.Coordinate> emptyNeighborhood = new ArrayList<Ply.Coordinate>(4);
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
        for (char columnLabel : COLUMN_LABELS)
            for (char rowLabel : ROW_LABELS)
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
        for (char columnCursor : COLUMN_LABELS) {
            sb.append(' ')
                    .append(columnCursor);
        }

        //header
        sb.append(separatorLine);
        for (char rowCursor : ROW_LABELS) {
            //line - pawn
            sb.append(rowCursor)
                    .append('|');
            for (char columnCursor : COLUMN_LABELS) {
                sb.append(getPawn(columnCursor,rowCursor))
                        .append('|');
            }
            //line - separator
            sb.append(separatorLine);
        }

        return sb.toString();
    }

    @Override
    public final boolean equals(Object that) {
        return that instanceof AbstractBoard
                && that.hashCode() == this.hashCode();
    }
}
