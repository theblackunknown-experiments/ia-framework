/*
 * #%L
 * Core Framework Project
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
 * Representation of a player
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public abstract class AbstractPlayer
        implements IPlayer {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AbstractPlayer.class.getCanonicalName());

    /**
     * ID
     */
    private int identifier = -1;

    public void setIdentifier(int id) {
        this.identifier = id;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getIdentifier() {
        return identifier;
    }
}
