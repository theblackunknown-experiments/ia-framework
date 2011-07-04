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
        //task loop
        while (!isOff()) {
            try {
                safeWait();

                //referee notified us
                require(player != null, "Player not set after referee notification");

                //let the player play
                setLastPly(player.play(context, rules));

                //notify referee
                RefereeWorker.getInstance().interrupt(GameEvent.PLAYER_PLY_ENTERED);

            } catch (InterruptedException e) {
                switch (getInterruptionStatus()) {
                    case NO_MORE_TIME://you just lose the game
                    case GAME_END://Game ended : soft end
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
