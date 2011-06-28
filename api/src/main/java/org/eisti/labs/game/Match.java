package org.eisti.labs.game;

import org.eisti.labs.util.Tuple;
import org.eisti.labs.util.Validation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.eisti.labs.util.Validation.require;

/**
 * Main engine
 *
 * @author MACHIZAUD Andréa
 * @version 24/06/11
 */
public class Match
        implements GameProperties {

    public static void main(String[] args)
            throws GameException {

        IBoard board = null;
        GameContext gameContext = null;
        IReferee referee = null;
        IPlayer[] players = null;
        long timeGranted = -1;

        // 1 - check parameter
        //TODO Refactor checking against rights restrictions systems (i.e WebStart)
        String fileParameter = System.getProperty(PROPERTY_FILE_PARAMETER);

        String refereeClazzImplementation = null;
        String[] playersClassImplementation = null;
        String boardClazzImplementation = null;
        String gameContextClazzImplementation = null;

        try {
            //ignore command-line parameter if a file is provided
            if (fileParameter != null) {
                Properties fileProperties = new Properties();
                fileProperties.load(new FileReader(fileParameter));
                /*==========================================================================
                  REFEREE - early instantiate because number of players piece of information
                  ==========================================================================*/
                {
                    refereeClazzImplementation = validate(REFEREE_KEY,
                            fileProperties.getProperty(REFEREE_KEY));
                    referee = (IReferee) Class.forName(refereeClazzImplementation)
                            .getConstructor()
                            .newInstance();
                }
                /*=========================================================================
                                       BOARD
                =========================================================================*/
                {
                    boardClazzImplementation = validate(BOARD_CLASS_KEY,
                            fileProperties.getProperty(BOARD_CLASS_KEY));
                }
                /*=========================================================================
                                       GAME CONTEXT
                =========================================================================*/
                {
                    gameContextClazzImplementation = validate(GAME_CONTEXT_CLASS_KEY,
                            fileProperties.getProperty(GAME_CONTEXT_CLASS_KEY));
                }
                /*=========================================================================
                                       GRANTED TIME
                  =========================================================================*/
                {
                    timeGranted = Long.parseLong(validate(PLAY_TIME_KEY,
                            fileProperties.getProperty(PLAY_TIME_KEY)));
                }
                /*=========================================================================
                                         PLAYERS
                  =========================================================================*/
                {
                    int playerNumber = referee.getNumberOfPlayer();
                    playersClassImplementation = new String[playerNumber];
                    //fill player container
                    for (int i = playerNumber; i-- > 0; ) {
                        //fetch player implementation class
                        Integer playerID = i + 1;
                        String playerKey = String.format(PLAYER_DESCRIPTION_TEMPLATE, playerID);
                        playersClassImplementation[i] = validate(playerKey,
                                fileProperties.getProperty(playerKey));
                    }
                }

            }//command line parameter
            else {
                if (args.length < 4) // at least one referee, one board, and two players
                    throw new Validation.UnsatisfiedCheck("Not enough argument.\n" +
                            USAGE);

                /*==========================================================================
                  REFEREE - early instantiate because number of players piece of information
                  ==========================================================================*/
                {
                    refereeClazzImplementation = validate(REFEREE_KEY, args[0]);
                    referee = (IReferee) Class.forName(refereeClazzImplementation)
                            .getConstructor()
                            .newInstance();
                }
                /*=========================================================================
                                     BOARD
                =========================================================================*/
                {
                    boardClazzImplementation = validate(BOARD_CLASS_KEY, args[1]);
                }
                /*=========================================================================
                                       GAME CONTEXT
                =========================================================================*/
                {
                    gameContextClazzImplementation = validate(GAME_CONTEXT_CLASS_KEY, args[2]);
                }
                /*=========================================================================
                                       GRANTED TIME
                  =========================================================================*/
                {
                    timeGranted = Long.parseLong(validate(PLAY_TIME_KEY, args[3]));
                }
                /*=========================================================================
                                         PLAYERS
                  =========================================================================*/
                {
                    int argOffset = 4;
                    int playerNumber = referee.getNumberOfPlayer();
                    playersClassImplementation = new String[playerNumber];
                    //fill player container
                    for (int i = argOffset;
                         i < args.length;
                         i++) {
                        //fetch player implementation class
                        Integer playerID = i - 1;
                        String playerKey = String.format(PLAYER_DESCRIPTION_TEMPLATE, playerID);
                        playersClassImplementation[i - argOffset] = validate(playerKey, args[i]);
                    }
                }
            }

            /*=========================================================================
                                   BASIC VERIFICATION
            =========================================================================*/
            require(refereeClazzImplementation != null, "Undetected operations which result in no referee");
            require(playersClassImplementation != null, "Undetected operations which result in no players");
            require(boardClazzImplementation != null, "Undetected operations which result in no board");
            require(timeGranted != -1, "Undetected operations which result in no time argument");
            /*=========================================================================
                                   INSTANTIATION
              =========================================================================*/
            /*=========================================================================
                                 BOARD
            =========================================================================*/
            {
                board = (IBoard) Class.forName(boardClazzImplementation)
                        .getConstructor()
                        .newInstance();
            }
            /*=========================================================================
                                   PLAYERS
            =========================================================================*/
            {
                players = new IPlayer[playersClassImplementation.length];
                for (int i = players.length; i-- > 0; ) {
                    String playerClazz = playersClassImplementation[i];
                    int playerID = i;
                    //create player
                    players[i] = (IPlayer) Class.forName(playerClazz)
                            .getConstructor()
                            .newInstance();
                    players[i].setIdentifier(playerID);
                }
                //available for GC
                playersClassImplementation = null;
            }

            /*=========================================================================
                                INITIALIZE GAME
              =========================================================================*/

            Duration[] playersTime = new Duration[players.length];
            Arrays.fill(
                    playersTime,
                    new Duration(timeGranted, TimeUnit.MILLISECONDS)
            );

            gameContext = (GameContext) Class.forName(gameContextClazzImplementation)
                    .getConstructor(
                            Duration.class,
                            IBoard[].class,
                            IPlayer[].class,
                            Duration[].class
                    )
                    .newInstance(
                            new Duration(0L, TimeUnit.MILLISECONDS),
                            new IBoard[]{board},
                            players,
                            playersTime
                    );

            //at last, we can play...
            run(referee, gameContext);
        } catch (Validation.UnsatisfiedCheck e) {
            throw new GameException(e.getLocalizedMessage(),e);
        } catch (NumberFormatException e) {
            throw new GameException("Error trying to convert a number", e);
        } catch (FileNotFoundException e) {
            throw new GameException("'" + fileParameter + "' could not be found", e);
        } catch (IOException e) {
            throw new GameException("IO error while reading '" + fileParameter + "'");
        } catch (InvocationTargetException e) {
            throw new GameException("Could not invoke given class", e);
        } catch (NoSuchMethodException e) {
            throw new GameException("Constructor not found", e);
        } catch (ClassNotFoundException e) {
            throw new GameException("Class not found, check your classpath", e);
        } catch (InstantiationException e) {
            throw new GameException("Could not instantiate given class", e);
        } catch (IllegalAccessException e) {
            throw new GameException("You don't have the right to call the `int` constructor", e);
        } catch (ClassCastException e) {
            throw new GameException("Given class does not implements `IPlayer` provided by the API", e);
        }
    }

    private static <T> T validate(String key, T item) throws Validation.UnsatisfiedCheck {
        if (item == null)
            throw new Validation.UnsatisfiedCheck(key + " key is missing");
        else
            return item;
    }

    //FIXME time ignored as first shot
    /*
     * IDEA : Reference should be shared between timer task,
     * (which are designed to update remaining time and alert when depleted)
     * and context (which are mean to only display it).
     *
     * My first concurrency experience... can't wait :-)
     */
    @SuppressWarnings("unchecked")
    private static void run(
            final IReferee referee,
            final GameContext initialContext) {


        //Global variables
        Tuple<IPlayer, Duration> currentPlayer = null;
        GameContext currentContext = initialContext;
        Set<Ply> allowedMoves = new HashSet<Ply>();
        Ply playerPly = null;
        boolean plyAccepted = false;

        //XXX : Check initial State ? Context ?
        require(currentContext.getState() == GameContext.GameState.NOT_YET_FINISH,
                "Game is already finish although nobody has played yet");

        //Game loop
        //I know that can be enhanced...
        do {
            //fetch current player
            currentPlayer = currentContext.getActivePlayer();
            //fetch possible moves
            allowedMoves.clear();
            allowedMoves.addAll(
                    referee.getLegalMoves(currentContext));

            do {
                if (System.getProperty("hints") != null) {
                    System.out.println("Allowed moves : " + allowedMoves);
                }
                //TODO Start chronometer here
                //ask the player for his ply
                playerPly = currentPlayer.getFirst().play(currentContext);
                //TODO End it here

                //check the ply against referee's authority
                plyAccepted = allowedMoves.contains(playerPly);
                if (!plyAccepted)
                    System.err.println(playerPly + " is not a valid move for this board");

            } //loop until a valid move is entered
            while (!plyAccepted);

            //update game context
            currentContext = referee
                    .generateNewContextFrom(currentContext, playerPly);

        } //loop until the game is finished
        while (currentContext.getState() == GameContext.GameState.NOT_YET_FINISH);

        //announce the final state conclusion
        switch (currentContext.getState()) {
            case DRAW:
                System.out.println("Draw Game !");
                break;
            case WIN:
                System.out.println(String.format(
                        "Player#%s won !",
                        currentContext.getActivePlayer()));
                break;
            case LOSE:
                System.out.println(String.format(
                        "Player#%s lose !",
                        currentContext.getActivePlayer()));
                break;
        }
        //some time statistics
        System.out.println("Game finished in " + currentContext.getElapsedTime());
    }
}

interface GameProperties {

    public static final String USAGE =
            "Usage : java -jar api-0.1.jar " +
                    "<referee impl> <board implementations> <context implementation> <time granted> [<playerX implementation> | ... ]";

    public static final String PROPERTY_FILE_PARAMETER = "game.properties";

    public static final String REFEREE_KEY = "referee";

    public static final String BOARD_CLASS_KEY = "board";

    public static final String PLAY_TIME_KEY = "time-granted";

    public static final String GAME_CONTEXT_CLASS_KEY = "game-context";

    public static final String PLAYER_DESCRIPTION_TEMPLATE = "player.%s";

}

class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}

