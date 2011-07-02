package org.eisti.labs.game.workers;

import org.eisti.labs.game.GameEvent;

/**
 * Dedicated worker for executing player's task
 *
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public class PlayerWorker
    extends Thread {

    private PlayerWorker(){}

    private static class PlayerWorkerHolder {
        private static final PlayerWorker instance =
            new PlayerWorker();
    }

    public static PlayerWorker getInstance(){
        return PlayerWorkerHolder.instance;
    }

    private enum PlayerTask
        implements Runnable {
        PLAYER_TASK;

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
                        default:
                            e.printStackTrace();
                    }

                }
            }
        }
    }
}
