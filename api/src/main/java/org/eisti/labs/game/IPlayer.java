package org.eisti.labs.game;

/**
 * Contract for player's actions
 *
 * @author MACHIZAUD Andréa
 * @version 18/06/11
 */
public interface IPlayer {

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
    public Ply play(GameContext context);

}
