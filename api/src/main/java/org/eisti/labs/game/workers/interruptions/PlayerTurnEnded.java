package org.eisti.labs.game.workers.interruptions;

import org.eisti.labs.game.IPlayer;

/**
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public class PlayerTurnEnded extends InterruptedException {
    private IPlayer player;

    public PlayerTurnEnded(IPlayer player) {
        super();
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }
}
