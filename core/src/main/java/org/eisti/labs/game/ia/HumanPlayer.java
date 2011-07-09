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
package org.eisti.labs.game.ia;

import org.eisti.labs.game.AbstractPlayer;
import org.eisti.labs.game.GameContext;
import org.eisti.labs.game.IRules;
import org.eisti.labs.game.Ply;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author MACHIZAUD Andréa
 * @version 27/06/11
 */
public final class HumanPlayer
        extends AbstractPlayer {

    //FIXME Limited to 9 rows  and 26 columns
    public static final Pattern COORDINATE_REGEXP = Pattern.compile("[A-Z][1-9]");
    public static final Pattern PASS_REGEXP = Pattern.compile("PASS");

    //TODO generalize when gui version available
    public final Scanner inputReader = new Scanner(System.in);

    @Override
    public Ply play(GameContext context, IRules rules) {
        try {
            String[] coordinates;
            do {
                System.out.println("Current game time : " + context.getElapsedTime());
                System.out.println("Your remaining time : " + context.getActivePlayer().getSecond());
                System.out.print("Where do you want to play ? ");
                String input = inputReader.nextLine();
                coordinates = input.split("[,]");
            } while (!valid(coordinates));

            System.out.println();
            if (coordinates.length == 1 && PASS_REGEXP.matcher(coordinates[0].trim()).matches())
                return Ply.PASS;
            else {
                Ply.Coordinate[] userMoves = new Ply.Coordinate[coordinates.length];
                for (int i = coordinates.length; i-- > 0; ) {
                    String userMove = coordinates[i].trim();
                    char column = userMove.charAt(0);
                    char row = userMove.charAt(1);

                    userMoves[i] = new Ply.Coordinate(column, row);
                }

                return new Ply(userMoves);
            }
        } catch (IllegalStateException e) {
            inputReader.close();
            return null;
        }
    }

    private static boolean valid(String[] input) {
        if (input == null) {
            System.err.println("No input");
            return false;
        } else if (input.length == 1 && PASS_REGEXP.matcher(input[0].trim()).matches()) {
            return true;
        } else {

            for (String coord : input)
                if (!COORDINATE_REGEXP.matcher(coord.trim()).matches()) {
                    System.err.println("Input must be like <Uppercase letter><Digit> e.g 'A3'.");
                    return false;
                }
            return true;
        }
    }

    @Override
    public String toString() {
        return "HumanPlayer#" + Long.toHexString(super.getIdentifier());
    }
}
