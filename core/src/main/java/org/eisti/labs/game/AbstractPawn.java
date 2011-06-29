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

import static org.eisti.labs.util.Validation.require;

/**
 * Abstract Pawn
 *
 * @deprecated
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public class AbstractPawn
        implements IPawn {

    private String name;
    private IPlayer owner;
    private int identifier;

    public AbstractPawn(String name, int uid, IPlayer owner) {
        require(name != null && name.length() != 0, "Invalid parameter `name`");
        require(owner != null, "Invalid parameter `owner` : null");
        this.name = name;
        this.identifier = uid;
        this.owner = owner;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getIdentifier() {
        return identifier;
    }

    @Override
    public IPlayer getOwner() {
        return owner;
    }

    public boolean isOwner(IPlayer player) {
        return getOwner() == player;
    }


    @Override
    public int hashCode() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AbstractPawn
                && obj.hashCode() == this.hashCode();
    }
}
