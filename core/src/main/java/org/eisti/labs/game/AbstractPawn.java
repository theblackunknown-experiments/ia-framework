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
