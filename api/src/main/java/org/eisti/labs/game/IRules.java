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
package org.eisti.labs.game;

import java.util.Set;

/**
 * Instance which should regulate rules for a specific game.
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public interface IRules<B extends IBoard, C extends GameContext<B,C>> {

    /** Legal moves from this board */
    public Set<Ply> getLegalMoves(final C context);

    /** Generate sub-board based on given one where playerPly is applied */
    public C doPly(final C previousContext, final Ply playerPly);

    public int getNumberOfPlayer();
    public int getNumberOfTypedPawns();

    public int getPlayerMask();
    public int getPawnMask();
}
