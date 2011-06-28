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
    public Ply(Coordinate... moves) {
        require(moves.length > 0, "Empty ply, use Ply.PASS for a pass");
        this.positionRegistry = Arrays.copyOf(
                moves,
                moves.length);
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
                && obj.hashCode() == hashCode();
    }

    @Override
    public String toString() {
        if (isPass())
            return "PASS Ply";
        else if (positionRegistry.length == 1)
            return String.format(
                    "Ply(%c,%c)",
                    (char) ('A' + positionRegistry[0].getColumn()),
                    (char) ('1' + positionRegistry[0].getRow()));
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("Stroke(");
            for (Coordinate coord : positionRegistry)
                sb.append('[')
                        .append((char) ('A' + coord.getColumn()))
                        .append((char) ('1' + coord.getRow()))
                        .append(']');
            sb.append(')');
            return sb.toString();
        }
    }

    /**
     * Representation of a coordinate on a board
     */
    public static class Coordinate {
        private final int row;
        private final int column;
        private final String representation;

        public static Coordinate Coordinate(char column, char row) {
            return new Coordinate(column, row);
        }

        /**
         * Constructor with chess index based parameters
         */
        public Coordinate(char column, char row) {
            require('A' <= column, "column index out of expected range A-???");
            require('1' <= row, "row index out of expected range 0-???");
            this.column = column - 'A';
            this.row = row - '1';
            this.representation = String.format(
                    "Coordinate(%c,%c)",
                    column,
                    row);
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
