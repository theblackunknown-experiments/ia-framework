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
