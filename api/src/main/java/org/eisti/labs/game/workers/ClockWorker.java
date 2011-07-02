package org.eisti.labs.game.workers;

import org.eisti.labs.game.Clock;
import org.eisti.labs.game.GameEvent;
import org.eisti.labs.game.workers.interruptions.PlayerTurnEnded;

/**
 * Dedicated worker for spending clock's time
 *
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public class ClockWorker
        extends Thread {

    private ClockWorker() {
        super(ClockTaskHolder.CLOCK_TASK, "Clock Worker");
    }

    private static class ClockWorkerHolder {
        private static final ClockWorker instance =
                new ClockWorker();
    }

    public static ClockWorker getInstance() {
        return ClockWorkerHolder.instance;
    }

    /**
     * Core task
     */
    private enum ClockTaskHolder
            implements Runnable {
        CLOCK_TASK;

        private GameEvent interruptionStatus = null;
        private boolean off = false;
        private Clock currentClock = null;

        public boolean isOff() {
            return off;
        }

        public void setOff(boolean off) {
            this.off = off;
        }

        public Clock getCurrentClock() {
            return currentClock;
        }

        public void setCurrentClock(Clock currentClock) {
            this.currentClock = currentClock;
        }

        @Override
        public void run() {
            while (!isOff()) {
                try {
                    //No clock is assigned to this thread
                    if (currentClock == null)
                        wait();

                    //Empty given clock every seconds
                    //TODO Adjust time update
                    while (currentClock.getRemainingTime() > 0) {
                        currentClock.setRemainingTime(
                                currentClock.getRemainingTime() - 1000);
                        sleep(1000);
                    }

                    //jump out of while condition : no more time remaining
                    RefereeWorker.getInstance().setGameEvent(GameEvent.NO_MORE_TIME);
                    RefereeWorker.getInstance().interrupt();

                } catch (InterruptedException e) {
                    switch (interruptionStatus) {
                        case PLAYER_TURN_ENDED:
                            currentClock = null;//remove current clock
                            break;
                        default:
                            e.printStackTrace();
                    }
                }
            }
        }
    }
}
