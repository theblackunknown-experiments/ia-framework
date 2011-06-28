package org.eisti.labs.game;

import org.eisti.labs.util.Tuple;

import static org.eisti.labs.util.Tuple.zip;

/**
 * Keep record of current context
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public abstract class GameContext<B extends IBoard>
        implements Cloneable {

    /**
     * Player currently playing
     */
    private Tuple<IPlayer, Duration>[] players;
    /**
     * Time remaining before the game ends
     */
    private Duration elapsedTime;
    /**
     * Board history
     */
    private B[] history;

    /**
     * Default constructor records every indicator.
     * <b>Active player must be the first in the array</b>
     * <b>History is ordered from recent to elder</b>
     */
    public GameContext(
            Duration elapsedTime,
            B[] history,
            IPlayer[] playersInGame,
            Duration[] playersRemainingTime) {
        this.elapsedTime = elapsedTime;
        this.players = zip(playersInGame, playersRemainingTime);
        this.history = history;
    }

    /** Branch off purpose only */
    protected GameContext() {}

    public Tuple<IPlayer, Duration> getActivePlayer() {
        return players[0];
    }

    public Tuple<IPlayer, Duration>[] getPlayers() {
        return players;
    }

    public Duration getElapsedTime() {
        return elapsedTime;
    }

    public B getBoard() {
        return history[0];
    }

    /**
     * Historical of all previous that lead to this `Game`
     */
    public B[] getHistory() {
        return history;
    }

    /**
     * Game's state
     */
    abstract public GameState getState();

    /**
     * This method should return an empty context.
     * All fields contains in the superclass will be erased
     */
    abstract protected GameContext buildEmptyContext();

    /**
     * Generate derived context from this one
     */
    public GameContext branchOff(B board) {
        GameContext alike = buildEmptyContext();

        //pass remaining time reference
        alike.elapsedTime = elapsedTime; //FIXME Reference sharing ?

        //update board history
        IBoard[] previousHistory = getHistory();
        alike.history = new IBoard[previousHistory.length + 1];
        alike.history[0] = board;
        System.arraycopy(
                previousHistory, 0,
                alike.history, 1,
                previousHistory.length);

        //roll player turn
        Tuple<IPlayer,Duration>[] allPlayers = getPlayers();
        alike.players = new Tuple[allPlayers.length];
        System.arraycopy(
                allPlayers, 1,
                alike.players, 0,
                allPlayers.length - 1
        );
        alike.players[allPlayers.length - 1] = players[0];

        return alike;
    }

    /**
     * Internal clone
     */
    @Override
    protected GameContext clone() {
        try {
            return (GameContext) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("CloneException although class is Cloneable");
        }
    }

    /**
     * Informs on the current state and what is possible next.
     */
    public enum GameState {
        WIN,
        LOSE,
        DRAW,
        NOT_YET_FINISH
    }
}
