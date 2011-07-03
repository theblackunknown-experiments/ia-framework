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
package org.eisti.labs.game.workers;

import org.eisti.labs.game.*;
import org.eisti.labs.util.Tuple;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import static org.eisti.labs.util.Validation.require;

/**
 * Game Scheduler
 *
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public class RefereeWorker
        extends GameWorker {

    private GameContext context;
    private IRules rules;

    private RefereeWorker() {
        super("Referee Worker");
    }

    private static class RefereeWorkerHolder {
        private static final RefereeWorker instance =
                new RefereeWorker();
    }

    public static RefereeWorker getInstance() {
        return RefereeWorkerHolder.instance;
    }

    public void setContext(GameContext initialContext) {
        this.context = initialContext;
    }

    public void setRules(IRules rules) {
        this.rules = rules;
    }

    private static void printHints(OutputStream out, Set<Ply> plies) {
        if (System.getProperty("hints") != null) {
            if (out instanceof PrintStream)
                ((PrintStream) out).println(plies);
            else
                try {
                    out.write(plies.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private static void printHints(Writer out, Set<Ply> plies) {
        if (System.getProperty("hints") != null) {
            try {
                out.write(plies + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    boolean ready() {
        return rules != null & context != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void task() {
        ClockWorker clockWorker = ClockWorker.getInstance();
        PlayerWorker playerWorker = PlayerWorker.getInstance();

        clockWorker.setGameClock(context.getElapsedTime());
        //notify set-up ready
        clockWorker.process();

        playerWorker.setRules(rules);
        playerWorker.process();

        //game loop
        Tuple<IPlayer, Clock> currentPlayer;

        Set<Ply> allowedPlies = new HashSet<Ply>();
        Ply enteredPly = null;
        boolean plyAccepted;

        gameloop:
        while (context.getState() == GameContext.GameState.RUNNING) {
            //fetch current player
            currentPlayer = context.getActivePlayer();
            //fetch legal moves
            allowedPlies.clear();
            allowedPlies.addAll(
                    rules.getLegalMoves(context));

            //ask for player's ply
            playerWorker.setPlayer(currentPlayer.getFirst());
            playerWorker.setContext(context);

            clockWorker.setPlayerClock(currentPlayer.getSecond());
            plyAccepted = false;
            while (!plyAccepted) {
                try {
                    //little help
                    printHints(System.out, allowedPlies);

                    //wake up clock task
                    clockWorker.process();
                    //wake up player task
                    playerWorker.process();

                    //wait for GameEvent : either ply entered or time run out
                    safeWait();

                } //referee is woke up by an interruption
                catch (InterruptedException e) {
                    System.out.println(getName() + " interrupted " + getInterruptionStatus());
                    switch (getInterruptionStatus()) {
                        //player ply entered, check for validity
                        case PLAYER_PLY_ENTERED:
                            //checking
                            enteredPly = playerWorker.getLastPly();
                            require(enteredPly != null, "Error at player's ply registration");
                            plyAccepted = allowedPlies.contains(enteredPly);
                            if (!plyAccepted)
                                System.err.println(enteredPly + " is not a valid move for this board");
                            else
                                //immediately stop clock
                                clockWorker.interrupt(GameEvent.PLAYER_PLY_ENTERED);
                            break;
                        // no more time remaining for current player, announce loser
                        case NO_MORE_TIME:
                            //immediately stop player
                            playerWorker.interrupt(GameEvent.NO_MORE_TIME);
                            System.out.println(String.format(
                                    "%s lose due to lack of time !",
                                    currentPlayer.getFirst()
                            ));
                            break gameloop;
                        //fallback case
                        default:
                            System.err.println("Unexpected error status when waiting for user input : "
                                    + getInterruptionStatus());
                            e.printStackTrace();
                            break gameloop;
                    }
                }
            }

            require(enteredPly != null, "Failed at assigning value at player's ply");

            //user ply is valid, apply it to the board and go on
            context = rules.doPly(context, enteredPly);
        }

        //shutdown workers
        clockWorker.interrupt(GameEvent.GAME_END);
        playerWorker.interrupt(GameEvent.GAME_END);

        //check if game should goes on
        switch (context.getState()) {
            case WIN:
                System.out.println(String.format(
                        "%s won !",
                        context.getActivePlayer().getFirst()));
                break;
            case DRAW:
                System.out.println("Draw Game !");
                break;
            case LOSE:
                System.out.println(String.format(
                        "%s lose !",
                        context.getActivePlayer().getFirst()));
                break;
        }
        //some time statistics
        System.out.println("Game finished in " + context.getElapsedTime());
    }
}
