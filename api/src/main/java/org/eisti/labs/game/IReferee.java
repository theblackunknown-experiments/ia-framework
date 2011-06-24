package org.eisti.labs.game;

import java.util.List;

/**
 * Instance which should regulate rules for a specific game.
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
public interface IReferee<B extends IBoard, C extends GameContext<B>> {

    /** Legal moves from this board */
    public List<Ply> getLegalMoves(C context);

    /** Generate sub-board based on given one where playerPly is applied */
    public B generateSubGame(B previousBoard, Ply playerPly);

    /** Current context of the game */
    public GameContext getCurrentContext();

    public void setPlayers(IPlayer... playersDescription);
    public IPlayer[] getPlayers();

    public int getNumberOfPlayer();
    public int getNumberOfTypedPawns();

    public int getPlayerMask();
    public int getPawnMask();
}
