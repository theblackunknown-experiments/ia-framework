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
    private final int identifier;

    /**
     * Default constructor, identifier provided.
     */
    protected AbstractPlayer(int id) {
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
