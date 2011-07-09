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

import org.eisti.labs.game.Clock;

import static org.eisti.labs.util.Validation.require;

/**
 * Dedicated worker for spending clock's time
 *
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public class ClockWorker
        extends GameWorker {

    private Clock currentClock = null;
    private Clock gameClock;

    //every 0.01sec
    private static final long CLOCK_UPDATE_INTERVAL = 10L;

    private ClockWorker() {
        super("Clock Worker");
    }

    private static class ClockWorkerHolder {
        private static final ClockWorker instance =
                new ClockWorker();
    }

    public static ClockWorker getInstance() {
        return ClockWorkerHolder.instance;
    }

    public void setGameClock(Clock gameClock) {
        this.gameClock = gameClock;
    }

    public void setPlayerClock(Clock playerClock) {
        this.currentClock = playerClock;
    }

    @Override
    boolean ready() {
        return gameClock != null;
    }

    @Override
    public void task() {
        while (!isOff()) {
            try {
                safeWait();

                require(gameClock != null,
                        "Clock not set after referee notification");
                require(currentClock != null,
                        "Clock not set after referee notification");

                //Time goes on...
                while (currentClock.getTime() > 0) {
                    System.err.println("Game's time : " + gameClock);
                    System.err.println("Player's remaining time : " + currentClock);
                    currentClock.setTime(
                            currentClock.getTime() - CLOCK_UPDATE_INTERVAL);
                    gameClock.setTime(
                            gameClock.getTime() + CLOCK_UPDATE_INTERVAL);
                    sleep(CLOCK_UPDATE_INTERVAL);
                    if (Thread.interrupted())
                        throw new InterruptedException();
                }

                //time's depletion
                RefereeWorker.getInstance().interrupt(GameEvent.NO_MORE_TIME);

            } catch (InterruptedException e) {
                switch (getInterruptionStatus()) {
                    case PLAYER_PLY_ENTERED:
                        //do nothing, just stop spending time
                        break;
                    case GAME_END:
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
