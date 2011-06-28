package org.eisti.labs.game;

import org.eisti.labs.util.Tuple;

import java.util.LinkedList;
import java.util.Queue;

import static org.eisti.labs.util.Validation.nullPresent;
import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
abstract public class AbstractReferee<B extends IBoard, C extends GameContext<B>>
        implements IReferee<B, C> {

    private int playerMask;
    private int pawnMask;

    private int playerBitCursor;
    private int pawnBitCursor;

    protected AbstractReferee() {
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
        playerBitCursor = 1;
        while (numberOfPlayer > Math.pow(2, playerBitCursor))
            playerBitCursor++;

        playerMask = (1 << playerBitCursor) - 1;

        pawnBitCursor = 1;
        while (numberOfTypedPawns > Math.pow(2, pawnBitCursor))
            pawnBitCursor++;

        pawnMask = ((1 << pawnBitCursor) - 1) << playerBitCursor;
    }

    public int getPlayerMask() {
        return playerMask;
    }

    public int getPawnMask() {
        return pawnMask;
    }

    public int getOwnerID(int pawnID) {
        return pawnID & getPlayerMask();
    }

    public int getPawnTypeID(int pawnID) {
        return (pawnID & getPawnMask()) >> pawnBitCursor;
    }

}
