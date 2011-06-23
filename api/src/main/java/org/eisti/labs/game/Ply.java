package org.eisti.labs.game;

import java.util.Arrays;

import static org.eisti.labs.util.Validation.require;

/**
 * Player's ply
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public class Ply {

    public static final Ply PASS = new Ply();

    /**
     * Internal record of all moves contained
     */
    private final Coordinate[] positionRegistry;

    private Ply() {
        positionRegistry = null;
    }

    /**
     * Default constructor for any ply,
     * moves should be inserted in the order
     * they are means to be applied to the game
     */
    public Ply(Coordinate firstPosition, Coordinate... otherPositions) {
        this.positionRegistry = new Coordinate[1 + otherPositions.length];
        positionRegistry[0] = firstPosition;
        if (otherPositions.length > 0)
            System.arraycopy(otherPositions, 0, positionRegistry, 1, otherPositions.length);
    }

    /**
     * Determine if the ply is a pass or not
     */
    public boolean isPass() {
        return this == PASS;
    }

    /**
     * Get pawn's source
     */
    public Coordinate getSource() {
        checkIsNotPassed();
        return positionRegistry[0];
    }

    /**
     * Get pawn's destination
     */
    public Coordinate getDestination() {
        checkIsNotPassed();
        return positionRegistry[positionRegistry.length - 1];
    }

    /**
     * Get all moves contained in this ply
     */
    public Coordinate[] getAllMoves() {
        return positionRegistry;
    }

    public Ply[] getAtomicPly() {
        if (positionRegistry.length < 3)
            return new Ply[]{this};
        else {
            Ply[] subPly = new Ply[positionRegistry.length - 1];
            int cursor = 0;
            Coordinate from, to;
            do {
                from = positionRegistry[cursor];
                to = positionRegistry[cursor + 1];
                subPly[cursor] = new Ply(from, to);
                cursor++;
            } while ((cursor + 1) < positionRegistry.length);
            return subPly;
        }
    }

    /**
     * Helper method to check if it's legal to access to internal move array
     */
    private void checkIsNotPassed() {
        if (isPass())
            throw new IllegalStateException("A pass ply contains no moves");
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(positionRegistry);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Ply
                && ((Ply) obj).hashCode() == hashCode();
    }

    /**
     * Representation of a coordinate on a board
     */
    public static class Coordinate {
        private final int row;
        private final int column;
        private final String representation;

        public static Coordinate Coordinate(int row, int column) {
            return new Coordinate(row, column);
        }

        /**
         * Constructor with array index based parameter
         */
        public Coordinate(int row, int column) {
            require(row >= 0, "row index negative");
            require(column >= 0, "column index negative");
            this.row = row;
            this.column = column;
            this.representation = String.format(
                    "Coordinate(%s,%s)",
                    String.valueOf(row),
                    String.valueOf(column));
        }

        /**
         * Constructor with chess index based parameters
         */
        public Coordinate(char row, char column) {
            require('0' <= row, "row index out of expected range 0-???");
            require('A' <= column, "column index out of expected range A-???");
            this.row = row - '0';
            this.column = column - 'A';
            this.representation = String.format(
                    "Coordinate(%s,%s)",
                    String.valueOf(column),
                    String.valueOf(row));
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        @Override
        public String toString() {
            return representation;
        }

        @Override
        public int hashCode() {
            return 41 * (
                    41 + row
            ) + column;
        }
    }

}
