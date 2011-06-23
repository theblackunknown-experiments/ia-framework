package org.eisti.labs.game;

import org.eisti.labs.util.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.eisti.labs.util.Validation.nullPresent;
import static org.eisti.labs.util.Validation.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
abstract public class AbstractReferee<B extends IBoard, C extends GameContext<B>>
        implements IReferee<B, C> {

    private IPlayer[] players;
    private int playerTurnToken = 0;//player that start the game is the first in the previous array

    private int playerMask;
    private int pawnMask;

    protected AbstractReferee(
            IPlayer... playersDescription) {
        /*
         * a pawn ID contains informations about
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

        //try to automatically adujst the number of necessary bits
        int playerBitCursor = 1;
        while (numberOfPlayer > Math.pow(2, playerBitCursor))
            playerBitCursor++;

        playerMask = (1 << playerBitCursor) - 1;

        int pawnBitCursor = 1;
        while (numberOfTypedPawns > Math.pow(2, pawnBitCursor))
            pawnBitCursor++;

        pawnMask = ((1 << pawnBitCursor) - 1) << playerBitCursor;

        require(playersDescription.length != 0, "No player provided");
        require(!nullPresent(playersDescription), "Null player provided");

        players = Arrays.copyOf(
                playersDescription,
                playersDescription.length);
    }

    abstract protected IPlayer[] registerPlayers(Tuple<Integer, Class<IPlayer>>... players)
        //forwaded error cause constructor to fail if any problems
            throws
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            InstantiationException;

    @Override
    public IPlayer[] getPlayers() {
        return players;
    }

    public IPlayer[] getOrderedPlayers() {
        if (playerTurnToken != 0) {
            IPlayer[] orderedPlayers = new IPlayer[players.length];
            //copy elder players
            System.arraycopy(
                    players, 0,
                    orderedPlayers, players.length - playerTurnToken,
                    playerTurnToken);
            //copy recent players
            System.arraycopy(
                    players, playerTurnToken,
                    orderedPlayers, 0,
                    players.length - playerTurnToken);
            return orderedPlayers;
        } else
            return players;
    }

    public IPlayer getActivePlayer() {
        return players[playerTurnToken];
    }

    public void slidePlayersTurn() {
        playerTurnToken = (playerTurnToken + 1) % players.length;
    }

    protected Ply translate(IBoard.ICase[] coordinateStroke) {
        if (coordinateStroke.length == 1)
            return new Ply(coordinateStroke[0].getPosition());
        else {
            Ply.Coordinate[] argument = new Ply.Coordinate[coordinateStroke.length - 1];
            for (int i = 1; i < coordinateStroke.length; i++)
                argument[i - 1] = coordinateStroke[i].getPosition();
            return new Ply(coordinateStroke[0].getPosition(), argument);
        }
    }

    public int getPlayerMask() {
        return playerMask;
    }

    public int getPawnMask() {
        return pawnMask;
    }

    //FIXME Please test it further...
    public int getOwnerID(int pawnID) {
        return pawnID & getPlayerMask();
    }

    //FIXME Please test it further...
    public int getPawnTypeID(int pawnID) {
        return (pawnID & getPawnMask()) >> getNumberOfTypedPawns();
    }

}
