package org.eisti.labs.game;

import java.util.List;
import java.util.Set;

/**
 * Instance which should regulate rules for a specific game.
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public interface IReferee<B extends IBoard, C extends GameContext<B>> {

    /** Legal moves from this board */
    public Set<Ply> getLegalMoves(C context);

    /** Generate sub-board based on given one where playerPly is applied */
    public C generateNewContextFrom(C previousContext, Ply playerPly);

    public int getNumberOfPlayer();
    public int getNumberOfTypedPawns();

    public int getPlayerMask();
    public int getPawnMask();
}
