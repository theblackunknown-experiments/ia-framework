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

/**
 * @author MACHIZAUD Andréa
 * @version 7/3/11
 */
public interface GameConfiguration {

    /**
     * Provide the IBoard implementation to the framework
     * @return IBoard implementation
     */
    public String provideBoardClazz();

    /**
     * Provide the IRules implementation to the framework
     * @return IRules implementation
     */
    public String provideRulesClazz();

    /**
     * Provide the GameContext implementation to the framework
     * @return GameContext implementation
     */
    public String provideContextClazz();

    /**
     * A shutdown hook to force process or task to end
     */
    public void shutdownHook();

    /**
     * <p>Determine in which order player are going to play</p>
     * <p>First in array will be the first to play</p>
     * @param players - player array, order follow command-line parameters
     * @return player ordered in game's turn
     */
    public IPlayer[] orderedPlayers(final IPlayer[] players);
}
