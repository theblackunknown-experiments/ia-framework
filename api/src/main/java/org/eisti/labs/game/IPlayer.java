/*
 * #%L
 * API Interface Project
 * %%
 * Copyright (C) 2011 L@ris's Labs
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

/**
 * Contract for player's actions
 *
 * @author MACHIZAUD Andréa
 * @version 18/06/11
 */
public interface IPlayer {

    public void setIdentifier(int id);
    /**
     * Player's name
     */
    public int getIdentifier();

    /**
     * Identifier set by referee
     */
//    public int getIdentifier();
    /**
     * Pawn movement from a player
     */
    public Ply play(GameContext context, IRules rules);

}
