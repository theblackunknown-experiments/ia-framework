package org.eisti.labs.ia;

import org.eisti.labs.game.GameContext;
import org.eisti.labs.game.IPlayer;

/**
 * @author MACHIZAUD Andréa
 * @version 28/06/11
 */
public interface IBot
        extends IPlayer {

    /**
     * Return a score evaluating the current game's situation
     */
    public double evaluate(GameContext context);

}
