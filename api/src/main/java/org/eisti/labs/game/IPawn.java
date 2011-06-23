package org.eisti.labs.game;

/**
 * Tag interface for Pawn
 *
 * @deprecated
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public interface IPawn {

    /**
     * Pawn's name
     */
    public String getName();

    /**
     * Hexadecimal representation
     */
    public int getIdentifier();

    public IPlayer getOwner();

}
