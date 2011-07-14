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

import java.awt.*;

/**
 * Tag interface for Game
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public interface IBoard
        extends Iterable<Ply.Coordinate> {

    /**
     * Unique representation of a case where no pawn is set
     */
    public static final int NO_PAWN = 0x0;

    /**
     * Game's dimension
     */
    public Dimension getDimension();

    /**
     * Get the pawn at given position of null if nothing found at that position
     *
     * @param column - column label
     * @param row    - row label
     * @return pawn at that location
     */
    public int getPawn(final String column, final String row);

    /**
     * Get the pawn at given position of null if nothing found at that position
     *
     * @param coordinate - board's coordinate
     * @return pawn at that location
     */
    public int getPawn(final Ply.Coordinate coordinate);

    /**
     * Set the pawn at given position of null if nothing found at that position
     *
     * @param column - column label
     * @param row    - row label
     * @param pawnID - pawn to set at given position
     */
    public void setPawn(final String column, final String row, final int pawnID);

    /**
     * Set the pawn at given position of null if nothing found at that position
     *
     * @param coordinate - board's coordinate
     * @param pawnID     - pawn to set at given position
     */
    public void setPawn(final Ply.Coordinate coordinate, final int pawnID);

    /* Helper for iterate over row|columns */

    /**
     * Get the label of board's first row
     *
     * @return label of board's first row
     */
    public String getFirstRowLabel();

    /**
     * Get the label of board's last row
     *
     * @return label of board's last row
     */
    public String getLastRowLabel();

    /**
     * Get the label of board's first column
     *
     * @return label of board's first column
     */
    public String getFirstColumnLabel();

    /**
     * Get the label of board's last column
     *
     * @return label of board's last column
     */
    public String getLastColumnLabel();

    /**
     * hash method
     */
    public int hashCode();
}
