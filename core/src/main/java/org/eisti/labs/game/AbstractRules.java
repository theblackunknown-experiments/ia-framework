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

import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
abstract public class AbstractRules<B extends IBoard, C extends GameContext<B, C>>
        implements IRules<B, C> {

    private final int playerMask;
    private final int pawnMask;

//    private int playerBitCursor;
    private int pawnBitCursor;

    protected AbstractRules() {
        /*
         * a pawn ID contains information about
         *  - which player owns that pawn
         *  - which type of pawn it is
         * and is represented as an int.
         *
         * We must ensure that we contains enough bit of information to do it
         */
        int numberOfPlayer = getNumberOfPlayer();
        int numberOfTypedPawns = (getNumberOfTypedPawns() + 1); // We must keep a no_pawn id type
        require(numberOfPlayer + numberOfTypedPawns < 32,
                "Not enough bit of information to represent playerID and pawnType within actual representation");

        //try to automatically adjust the number of necessary bits
        int playerBitCursor = 1;
        while (numberOfPlayer > Math.pow(2, playerBitCursor))
            playerBitCursor++;

        playerMask = (1 << playerBitCursor) - 1;

        pawnBitCursor = 1;
        while (numberOfTypedPawns > Math.pow(2, pawnBitCursor))
            pawnBitCursor++;

        pawnMask = ((1 << pawnBitCursor) - 1) << playerBitCursor;
    }

    public final int getPlayerMask() {
        return playerMask;
    }

    public final int getPawnMask() {
        return pawnMask;
    }

    public final int getOwnerID(final int pawnID) {
        return pawnID & getPlayerMask();
    }

    public final int getPawnTypeID(final int pawnID) {
        return (pawnID & getPawnMask()) >> pawnBitCursor;
    }

}
