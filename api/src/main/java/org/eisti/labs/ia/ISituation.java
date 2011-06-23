package org.eisti.labs.ia;

import org.eisti.labs.game.GameContext;

/**
 * Game situation that can be evaluated for compatibility with tree-like strategy
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public interface ISituation {

    /**
     * Return a score evaluating the current game's situation
     */
    public double evaluate(GameContext context);

}
