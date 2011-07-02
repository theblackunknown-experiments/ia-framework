package org.eisti.labs.game.workers;

import org.eisti.labs.game.Clock;
import org.eisti.labs.game.GameEvent;

/**
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public class RefereeWorker
        extends Thread {

    private RefereeWorker() {
        super(RefereeTask.REFEREE_TASK,"Referee Worker");
    }

    private static class RefereeWorkerHolder {
        private static final RefereeWorker instance =
                new RefereeWorker();
    }

    public static RefereeWorker getInstance() {
        return RefereeWorkerHolder.instance;
    }

    public void setGameEvent(GameEvent event) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    private enum RefereeTask
            implements Runnable {
        REFEREE_TASK;

        private GameEvent interruptionStatus = null;
        private boolean off = false;

        public boolean isOff() {
            return off;
        }

        public void setOff(boolean off) {
            this.off = off;
        }

        @Override
        public void run() {
            while (!isOff()) {
                try {
                    //TODO Game logic
                } catch (InterruptedException e) {
                    switch (interruptionStatus) {
                        case PLAYER_TURN_ENDED:
                            //TODO Roll players, Notify ClockWorker then PlayerWorker
                            break;
                        case NO_MORE_TIME:
                            //TODO Declare winner/losers
                            break;
                        default:
                            e.printStackTrace();
                    }

                }
            }
        }
    }
}
