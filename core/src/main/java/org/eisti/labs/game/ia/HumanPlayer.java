package org.eisti.labs.game.ia;

import org.eisti.labs.game.AbstractPlayer;
import org.eisti.labs.game.GameContext;
import org.eisti.labs.game.Ply;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author MACHIZAUD Andréa
 * @version 27/06/11
 */
public class HumanPlayer
        extends AbstractPlayer {

    //FIXME Limited to 9 rows  and 26 columns
    public static final Pattern COORDINATE_REGEXP = Pattern.compile("[A-Z][1-9]");
    public static final Pattern PASS_REGEXP = Pattern.compile("PASS");

    //TODO generalize when gui version available
    public static final Scanner inputReader = new Scanner(System.in);

    //TODO Handle time spent here ? Or separate thread ?
    @Override
    public Ply play(GameContext context) {
        System.out.println("Current board is :\n" + context.getBoard());
        String[] coordinates;
        System.out.println("Your turn Player#" + getIdentifier());
        do {
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
}
