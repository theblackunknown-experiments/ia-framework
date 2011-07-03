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

/**
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
abstract public class GameWorker
        extends Thread {

    private GameEvent interruptionStatus = null;
    private boolean off = false;

    protected GameWorker(String name) {
        super(name);
        start();
    }

    public final GameEvent getGameEvent() {
        return interruptionStatus;
    }

    public final void setGameEvent(GameEvent event) {
        this.interruptionStatus = event;
    }

    protected final GameEvent getInterruptionStatus() {
        return interruptionStatus;
    }

    public final boolean isOff() {
        return off;
    }

    public final void setOff(boolean off) {
        this.off = off;
    }

    /**
     * Communication between GameWorker Thread
     */
    public synchronized final void interrupt(GameEvent event) {
        setGameEvent(event);
        IOInterruption();
        interrupt();
    }

    protected void IOInterruption(){}

    public synchronized final void process() {
        notify();
    }

    public synchronized final void safeWait() throws InterruptedException {
        wait();
    }

    abstract boolean ready();

    @Override
    public final void run(){
        try {
            while (!ready())
                safeWait();
            task();
        } catch (InterruptedException e) {
            throw new Error(getName() + " interrupted while waiting for set-up");
        }
    }

    abstract public void task();

    protected enum GameEvent {
        PLAYER_PLY_ENTERED,
        NO_MORE_TIME,
        GAME_END
    }

}
