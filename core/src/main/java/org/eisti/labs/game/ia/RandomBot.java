/*
 * #%L
 * Core Framework Project
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
package org.eisti.labs.game.ia;

import org.eisti.labs.game.AbstractPlayer;
import org.eisti.labs.game.GameContext;
import org.eisti.labs.game.IRules;
import org.eisti.labs.game.Ply;
import org.eisti.labs.ia.IBot;

import java.util.Random;
import java.util.Set;

/**
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public final class RandomBot
        extends AbstractPlayer
        implements IBot {

    private Random random = new Random();

    @Override
    public double evaluate(GameContext context) {
        throw new UnsupportedOperationException(
                "RandomBot is just too dumb to try to evaluate a board...");
    }

    /**
     * Ply a random ply within legal ones
     */
    @Override
    @SuppressWarnings("unchecked")
    public final Ply play(GameContext context, IRules rules) {
        final Set<Ply> legalMovesSet = rules.getLegalMoves(context);
        final Ply[] legalMoves = legalMovesSet.toArray(new Ply[legalMovesSet.size()]);
        return legalMoves.length > 0
                ? legalMoves[random.nextInt(legalMoves.length)]
                : Ply.PASS;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + Integer.toHexString(getIdentifier());
    }
}
