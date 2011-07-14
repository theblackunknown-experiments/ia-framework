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

import org.eisti.labs.util.Tuple;

import static org.eisti.labs.util.Tuple.zip;

/**
 * Keep record of current context
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public abstract class GameContext<B extends IBoard, C extends GameContext>
        implements Cloneable {

    /**
     * Player currently playing
     */
    private Tuple<IPlayer, Clock>[] players;
    /**
     * Time remaining before the game ends
     */
    private Clock elapsedTime;
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
            Clock elapsedTime,
            B[] history,
            IPlayer[] playersInGame,
            Clock[] playersRemainingTime) {
        this.elapsedTime = elapsedTime;
        this.players = zip(playersInGame, playersRemainingTime);
        this.history = history;
    }

    /**
     * Branch off purpose only
     */
    protected GameContext() {
    }

    public final Tuple<IPlayer, Clock> getActivePlayer() {
        return players[0];
    }

    public final Tuple<IPlayer, Clock>[] getPlayers() {
        return players;
    }

    public final Clock getElapsedTime() {
        return elapsedTime;
    }

    public final B getBoard() {
        return history[0];
    }

    /**
     * Historical of all previous that lead to this `Game`
     */
    public final B[] getHistory() {
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
    abstract protected C buildEmptyContext();

    /**
     * Generate derived context from this one
     */
    public final C branchOff(B board) {
        C alike = buildEmptyContext();

        //pass remaining time reference
        alike.elapsedTime = elapsedTime;

        //update board history
        IBoard[] previousHistory = getHistory();
        alike.history = new IBoard[previousHistory.length + 1];
        alike.history[0] = board;
        System.arraycopy(
                previousHistory, 0,
                alike.history, 1,
                previousHistory.length);

        //roll player turn
        Tuple<IPlayer, Clock>[] allPlayers = getPlayers();
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
     * Allow to keep same perspective but with changing current player, but with player order respected
     */
    protected final C changePerspective(IPlayer newCurrentPlayer) {
        C alike = buildEmptyContext();

        //pass remaining time reference
        alike.elapsedTime = elapsedTime;

        //update board history
        IBoard[] previousHistory = getHistory();
        alike.history = new IBoard[previousHistory.length];
        System.arraycopy(
                previousHistory, 0,
                alike.history, 0,
                previousHistory.length);

        //roll player turn
        Tuple<IPlayer, Clock>[] allPlayers = getPlayers();
        int newPlayerIdx = -1;

        for (int i = allPlayers.length; i-- > 0; )
            if (allPlayers[i].getFirst() == newCurrentPlayer) {
                newPlayerIdx = i;
                break;
            }
        if (newPlayerIdx == -1)
            throw new IllegalStateException("Given player doesn't exists in this game : "
                    + newCurrentPlayer);
        alike.players = new Tuple[allPlayers.length];
        System.arraycopy(
                allPlayers, newPlayerIdx,
                alike.players, 0,
                allPlayers.length - newPlayerIdx
        );
        if (newPlayerIdx != 0)
            //FIXME Is it different if length is odd or even ?
            System.arraycopy(
                    allPlayers, 0,
                    alike.players,
                    allPlayers.length % 2 == 0
                            ? newPlayerIdx
                            : newPlayerIdx + 1,
                    newPlayerIdx
            );

        return alike;
    }

    /**
     * Internal clone
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final C clone() {
        try {
            return (C) super.clone();
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
        RUNNING
    }
}
