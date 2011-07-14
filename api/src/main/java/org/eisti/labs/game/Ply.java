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

import java.util.Arrays;

import static org.eisti.labs.util.Validation.require;

/**
 * Player's ply
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public final class Ply {

    /**
     * Unique representation of a pass ply, i.e a player's turn when no move is played
     */
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
                    "Ply(%s,%s)",
                    positionRegistry[0].getColumn(),
                    positionRegistry[0].getRow());
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("Stroke(");
            for (Coordinate coord : positionRegistry)
                sb.append('[')
                        .append(coord.getColumn())
                        .append(coord.getRow())
                        .append(']');
            sb.append(')');
            return sb.toString();
        }
    }

    /**
     * Representation of a coordinate on a board
     */
    public static final class Coordinate {
        private final String column;
        private final String row;
        private final String representation;

        public static Coordinate Coordinate(final String column, final String row) {
            return new Coordinate(column, row);
        }

        /**
         * Constructor with chess index based parameters
         */
        public Coordinate(final String column, final String row) {
            this.column = column;
            this.row = row;
            this.representation = String.format(
                    "Coordinate(%s,%s)",
                    column,
                    row);
        }

        public final String getRow() {
            return row;
        }

        public final String getColumn() {
            return column;
        }

        @Override
        public final String toString() {
            return representation;
        }

        @Override
        public final int hashCode() {
            return 41 * (
                    41 + row.hashCode()
            ) + column.hashCode();
        }

        /**
         * Column's label alphabet
         */
        public static final String[] ALPHABET = new String[]{
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
        };

        public static final String BEFORE_FIRST_ROW = "-1";
        public static final String AFTER_LAST_ROW = "+1";
        public static final String BEFORE_FIRST_COLUMN = "-1";
        public static final String AFTER_LAST_COLUMN = "+1";

        /**
         * Translate a column's label into a array index
         */
        public static int columnLabel2index(final String column) {
            switch (column.length())//Can on have a label column like "A" or "AA" like
            {
                case 1:
                    return column.charAt(0) - 'A';
                case 2:
                    return (column.charAt(0) - 'A' + 1) * ALPHABET.length
                            + column.charAt(1) - 'A';
                default:
                    throw new Error("Unexpected case column label size : " + column.length());
            }
        }

        /**
         * Translate a row's label into a array index
         */
        public static int rowLabel2index(final String row) {
            return Integer.parseInt(row) - 1;
        }

        /**
         * Translate a column index into a label
         */
        public static String columnIndex2Label(final int columnIndex) {
            switch (columnIndex) {
                case -1:
                    return BEFORE_FIRST_COLUMN;
                case Integer.MAX_VALUE:
                    return AFTER_LAST_COLUMN;
                default:
                    final String prefix = columnIndex < ALPHABET.length
                            ? ""
                            : ALPHABET[(columnIndex / ALPHABET.length) - 1];
                    return prefix + ALPHABET[columnIndex % ALPHABET.length];
            }
        }

        /**
         * Translate a row index into a label
         */
        public static String rowIndex2Label(final int rowIndex) {
            switch (rowIndex) {
                case -1:
                    return BEFORE_FIRST_ROW;
                case Integer.MAX_VALUE:
                    return AFTER_LAST_ROW;
                default:
                    return String.valueOf(rowIndex + 1);
            }
        }
    }

}
