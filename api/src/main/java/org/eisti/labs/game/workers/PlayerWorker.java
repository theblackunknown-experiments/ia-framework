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

import org.eisti.labs.game.GameContext;
import org.eisti.labs.game.IPlayer;
import org.eisti.labs.game.IRules;
import org.eisti.labs.game.Ply;

import java.util.concurrent.*;

import static org.eisti.labs.util.Validation.require;

/**
 * Dedicated worker for executing player's task
 *
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public class PlayerWorker
        extends GameWorker {

    private IPlayer player;
    private GameContext context;
    private IRules rules;
    volatile private Ply lastPly;

    private PlayerWorker() {
        super("Player Worker");
    }

    public void setLastPly(Ply lastPly) {
        this.lastPly = lastPly;
    }

    public Ply getLastPly() {
        return lastPly;
    }

    public void setRules(IRules rules) {
        this.rules = rules;
    }

    private static class PlayerWorkerHolder {
        private static final PlayerWorker instance =
                new PlayerWorker();
    }

    public static PlayerWorker getInstance() {
        return PlayerWorkerHolder.instance;
    }

    public void setPlayer(IPlayer player) {
        this.player = player;
    }

    public void setContext(GameContext context) {
        this.context = context;
    }

    @Override
    boolean ready() {
        return rules != null;
    }

    @Override
    public void task() {
        //sub-task runner
        ExecutorService ex = Executors.newSingleThreadExecutor();
        //future user's response
        Future<Ply> userInputReader = null;
        //task loop
        while (!isOff()) {
            try {
                safeWait();

                //referee notified us
                require(player != null, "Player not set after referee notification");

                //defers the user's response wait to the sub-task,
                // but keep a reference to the tas kso we can supervise it
                userInputReader = ex.submit(new Callable<Ply>() {
                    @Override
                    public Ply call() throws Exception {
                        return player.play(context, rules);
                    }
                });

                //wait for the response to be complete, and record it
                //implicit `wait` here (cf. future)
                setLastPly(userInputReader.get());

                //notify referee
                RefereeWorker.getInstance().interrupt(GameEvent.PLAYER_PLY_ENTERED);

            }//sub-task unexpected error
            catch (ExecutionException e) {
                switch (getInterruptionStatus()) {
                    default:
                        System.err.println("Unexpected error status when waiting for user input : "
                                + getInterruptionStatus());
                        e.printStackTrace();
                }
            }//interrupt happened, expected it to be on the future's `wait`
            catch (InterruptedException e) {
                switch (getInterruptionStatus()) {
                    case NO_MORE_TIME://you just lose the game
                    case GAME_END://Game ended : soft end
                        //cancel the user response wait
                        if (userInputReader != null) userInputReader.cancel(true);
                        //close the sub-task supervisor
                        ex.shutdownNow();
                        //close current thread
                        setOff(true);
                        break;
                    default:
                        System.err.println("Unexpected error status when waiting for user input : "
                                + getInterruptionStatus());
                        e.printStackTrace();
                }
            }
        }
    }
}
