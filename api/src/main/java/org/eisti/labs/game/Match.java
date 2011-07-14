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

import org.eisti.labs.game.workers.ClockWorker;
import org.eisti.labs.game.workers.PlayerWorker;
import org.eisti.labs.game.workers.RefereeWorker;
import org.eisti.labs.util.Validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import static org.eisti.labs.game.workers.GameWorker.GameEvent.GAME_END;
import static org.eisti.labs.util.Validation.require;

/**
 * Main engine
 *
 * @author MACHIZAUD Andréa
 * @version 24/06/11
 */
public final class Match
        implements GameProperties {

    public static void main(String[] args)
            throws GameException, InterruptedException {
        if (args.length == 1
                && (args[0].equals("-h") || args[0].equals("--help"))) {
            System.out.println(USAGE);
            System.exit(0);
        }


        IBoard board;
        GameContext gameContext;
        IRules rules;
        IPlayer[] players;
        long timeGranted;

        // 1 - check parameter
        //TODO Refactor checking against rights restrictions systems (i.e WebStart)
        try {
            GameConfiguration configuration;

            /*=========================================================================
                          LOAD GAME CONFIGURATION
            =========================================================================*/
            {
                Properties fileProperties = new Properties();
                fileProperties.load(
                        Match.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_PARAMETER));
                String configurationImplementation = validate(CONFIGURATION_KEY,
                        fileProperties.getProperty(CONFIGURATION_KEY));
                configuration = (GameConfiguration) Class.forName(configurationImplementation)
                        .getConstructor()
                        .newInstance();
            }

            String rulesClazzImplementation = configuration.provideRulesClazz();
            String boardClazzImplementation = configuration.provideBoardClazz();
            String gameContextClazzImplementation = configuration.provideContextClazz();
            String[] playersClassImplementation;

            /*==========================================================================
            REFEREE - early instantiate because number of players piece of information
            ==========================================================================*/
            {
                rules = (IRules) Class.forName(rulesClazzImplementation)
                        .getConstructor()
                        .newInstance();
            }

            if (args.length < 1 + rules.getNumberOfPlayer()) {
                System.err.println("Not enough argument.\n" +
                        USAGE);
                System.exit(0);
            }
            /*=========================================================================
                                 GRANTED TIME
            =========================================================================*/
            {
                timeGranted = Long.parseLong(validate(PLAY_TIME_KEY, args[0]));
            }
            /*=========================================================================
                                   PLAYERS
            =========================================================================*/
            {
                int argOffset = 1;
                int playerNumber = rules.getNumberOfPlayer();
                playersClassImplementation = new String[playerNumber];
                //fill player container
                for (int i = argOffset;
                     i < args.length;
                     i++) {
                    //fetch player implementation class
                    Integer playerID = i - 1;
                    String playerKey = String.format(PLAYER_DESCRIPTION_TEMPLATE, playerID);

                    String clazzArgument = validate(playerKey, args[i]);

                    playersClassImplementation[i - argOffset] = clazzArgument.contains(".")
                            ? clazzArgument
                            : BUILTIN_PLAYER_PACKAGE + clazzArgument;
                }
            }

            /*=========================================================================
                                   BASIC VERIFICATION
            =========================================================================*/
            require(rulesClazzImplementation != null, "Undetected operations which result in no rules");
            require(playersClassImplementation != null, "Undetected operations which result in no players");
            require(boardClazzImplementation != null, "Undetected operations which result in no board");
            require(timeGranted > 0, "Undetected operations which result in negative time argument");
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

                configuration.orderedPlayers(players);
            }

            /*=========================================================================
                                INITIALIZE GAME
              =========================================================================*/

            Clock[] playersTime = new Clock[players.length];
            for (int i = playersTime.length; i-- > 0; )
                playersTime[i] = new Clock(timeGranted);

            gameContext = (GameContext) Class.forName(gameContextClazzImplementation)
                    .getConstructor(
                            Clock.class,
                            IBoard[].class,
                            IPlayer[].class,
                            Clock[].class
                    )
                    .newInstance(
                            new Clock(0L),
                            new IBoard[]{board},
                            players,
                            playersTime
                    );

            //at last, we can play...
            run(rules, gameContext);

            configuration.shutdownHook();

            //Brutal exit because thread waiting on standard input cannot be closed...
            System.exit(0);
        } catch (Validation.UnsatisfiedCheck e) {
            throw new GameException(e.getLocalizedMessage(), e);
        } catch (NumberFormatException e) {
            throw new GameException("Error trying to convert a number", e);
        } catch (FileNotFoundException e) {
            throw new GameException("'" + PROPERTY_FILE_PARAMETER + "' were not found", e);
        } catch (IOException e) {
            throw new GameException("IO error while reading '" + PROPERTY_FILE_PARAMETER + "'");
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

    private static void run(
            final IRules rules,
            final GameContext initialContext) throws InterruptedException {
        require(initialContext.getState() == GameContext.GameState.RUNNING,
                "Game is already finish although nobody has played yet");

        //set up referee
        RefereeWorker referee = RefereeWorker.getInstance();

        referee.setContext(initialContext);
        referee.setRules(rules);

        //start game
        referee.process();

        RefereeWorker.getInstance().join();

        //shutdown workers
        ClockWorker.getInstance().interrupt(GAME_END);
        ClockWorker.getInstance().join();
        PlayerWorker.getInstance().interrupt(GAME_END);
        //ensure player worker really receive drug pill
        Thread.sleep(100);
        PlayerWorker.getInstance().interrupt(GAME_END);
        PlayerWorker.getInstance().join();
    }
}

interface GameProperties {

    public static final String USAGE =
            "Usage : java -jar {...}.jar <time granted> [<playerX implementation> | ... ]";

    public static final String PROPERTY_FILE_PARAMETER = "game.properties";

    public static final String CONFIGURATION_KEY = "configuration";

    public static final String PLAY_TIME_KEY = "time-granted";

    public static final String PLAYER_DESCRIPTION_TEMPLATE = "player.%s";

    public static final String BUILTIN_PLAYER_PACKAGE = "org.eisti.labs.game.ia.";

}

class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}

