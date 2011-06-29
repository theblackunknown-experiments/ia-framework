/*
 * #%L
 * API Interface Project
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

/**
 * Tag interface for Game
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public interface IBoard
    extends Iterable<IBoard.ICase> {

    /** Game's dimension */
    public Dimension getDimension();
    /** Get the pawn at given position of null if nothing found at that position */
    public ICase getCase(int row, int column);
    /** hash method */
    public int hashCode();

    public interface ICase {

        public static final int NO_PAWN = 0x0;

        public void setPawnID(int pawnID);

        public int getPawnID();

        public Ply.Coordinate getPosition();

    }
}
