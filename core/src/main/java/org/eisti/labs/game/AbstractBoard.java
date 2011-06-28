package org.eisti.labs.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.eisti.labs.util.Validation.require;

/**
 * General template for 2D board game
 *
 * @author MACHIZAUD Andréa
 * @version 17/06/11
 */
abstract public class AbstractBoard
        implements IBoard, Cloneable {

    /**
     * Board representation
     */
    protected final ICase[] board;
    /**
     * Dimension
     */
    private final Dimension boardDimension;


    private char[] COLUMN_LABELS;
    private char[] ROW_LABELS;

    /**
     * General constructor
     *
     * @param width  row size
     * @param height column size
     */
    protected AbstractBoard(int width, int height) {
        boardDimension = new Dimension(width, height);


        //FIXME width or height > 26

        COLUMN_LABELS = new char[width];
        for (char i = 0; i < width; i++)
            COLUMN_LABELS[i] = (char) ('A' + i);

        ROW_LABELS = new char[height];
        for (char i = 0; i < height; i++)
            ROW_LABELS[i] = (char) ('1' + i);

        board = new ICase[width * height];

        for (int i = height; i-- > 0; )
            for (int j = width; j-- > 0; )
                board[getBoardIndex(i, j)] = new Case(i, j);
        initializeBoard();
        require(boardInitialized(board),
                "Some case were not initialized");
    }

    /**
     * @inheritDoc
     */
    @Override
    public Dimension getDimension() {
        return boardDimension;
    }

    /**
     * @inheritDoc
     */
    public int getPawnID(int row, int column) {
        return getCase(row, column).getPawnID();
    }

    public int getPawnID(Ply.Coordinate coord) {
        require(coord != null, "Invalid argument coord : null");
        return getPawnID(coord.getRow(), coord.getColumn());
    }

    public final boolean isAt(Ply.Coordinate coord, int pawnID) {
        return getPawnID(coord) == pawnID;
    }

    @Override
    public ICase getCase(int row, int column) {
        if (!(0 <= row && row < getDimension().getHeight()))
            throw new ArrayIndexOutOfBoundsException("Out of bound row : " + row);
        if (!(0 <= column && column < getDimension().getWidth()))
            throw new ArrayIndexOutOfBoundsException("Out of bound column : " + column);
        return board[getBoardIndex(row, column)];
    }

    private int getBoardIndex(int row, int column) {
        return row * (int) (getDimension().getHeight()) + column;
    }

    public ICase[] getCaseAround(ICase center) {
        Collection<ICase> neighborhood = new ArrayList<ICase>(4);

        //vertical neighbor
        switch (center.getPosition().getRow()) {
            case 0: // first row
                neighborhood.add(
                        getCase(
                                center.getPosition().getRow() + 1,
                                center.getPosition().getColumn()));
                break;
            case 7: //last row
                neighborhood.add(
                        getCase(
                                center.getPosition().getRow() - 1,
                                center.getPosition().getColumn()));
                break;
            default: //otherwise
                neighborhood.add(
                        getCase(
                                center.getPosition().getRow() + 1,
                                center.getPosition().getColumn()));
                neighborhood.add(
                        getCase(
                                center.getPosition().getRow() - 1,
                                center.getPosition().getColumn()));

        }

        //horizontal neighbor
        switch (center.getPosition().getColumn()) {
            case 0: // first column
                neighborhood.add(
                        getCase(
                                center.getPosition().getRow(),
                                center.getPosition().getColumn() + 1));
                break;
            case 7: // last column
                neighborhood.add(
                        getCase(
                                center.getPosition().getRow(),
                                center.getPosition().getColumn() - 1));
                break;
            default: //otherwise
                neighborhood.add(
                        getCase(
                                center.getPosition().getRow(),
                                center.getPosition().getColumn() + 1));
                neighborhood.add(
                        getCase(
                                center.getPosition().getRow(),
                                center.getPosition().getColumn() - 1));

        }

        ICase[] result = new ICase[neighborhood.size()];
        neighborhood.toArray(result);
        return result;
    }

    public final ICase[] getFreeCaseAround(ICase center) {
        Collection<ICase> emptyNeighborhood = new ArrayList<ICase>(4);
        for (ICase neighbor : getCaseAround(center))
            if (neighbor.getPawnID() == ICase.NO_PAWN)
                emptyNeighborhood.add(neighbor);

        ICase[] result = new ICase[emptyNeighborhood.size()];
        emptyNeighborhood.toArray(result);
        return result;
    }

    /**
     * Board initialization method available for subclass
     */
    abstract protected void initializeBoard();


    @Override
    public Iterator<ICase> iterator() {
        return Arrays.asList(board).iterator();
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractBoard clone() {
        try {
            return (AbstractBoard) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("Clone exception although class is cloneable");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int width = getDimension().width;
        int height = getDimension().height;

        int gridSize = width * 2 + 1;

        String separatorLine = null;
        {
            StringBuilder separatorBuilder = new StringBuilder(gridSize + 2);
            separatorBuilder.append('\n');
            separatorBuilder.append(' ');
            for (int columnCursor = gridSize; columnCursor-- > 0; )
                separatorBuilder.append('-');
            separatorBuilder.append('\n');
            separatorLine = separatorBuilder.toString();
        }

        sb.append(' ');
        for (int columnCursor = 0; columnCursor < width; columnCursor++) {
            sb.append(' ')
                    .append(COLUMN_LABELS[columnCursor]);
        }

        //header
        sb.append(separatorLine);
        for (int lineCursor = 0; lineCursor < height; lineCursor++) {
            //line - pawn
            sb.append(ROW_LABELS[lineCursor])
                    .append('|');
            for (int columnCursor = 0; columnCursor < width; columnCursor++) {
                sb.append(getCase(lineCursor, columnCursor).getPawnID())
                        .append('|');
            }
            //line - separator
            sb.append(separatorLine);
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof AbstractBoard
                && that.hashCode() == this.hashCode();
    }

    /**
     * Internal representation of game's case
     */
    private class Case implements ICase {

        /**
         * Case coordinate
         */
        private Ply.Coordinate position;

        /**
         * Pawn on this case if any
         */
        private int occupantID;

        protected Case(int row, int column) {
            require(0 <= row && row < getDimension().getWidth(), "row index is out of board");
            require(0 <= column && column < getDimension().getHeight(), "column index is out of board");
            this.position = new Ply.Coordinate(
                    (char) (column + 'A'),
                    (char) (row + '1')
            );
        }

        public void setPawnID(int occupantID) {
            this.occupantID = occupantID;
        }

        @Override
        public int getPawnID() {
            return occupantID;
        }

        @Override
        public Ply.Coordinate getPosition() {
            return position;
        }

        @Override
        public int hashCode() {
            return occupantID == NO_PAWN
                    ? 0
                    : 41 * (
                    occupantID
            ) + position.hashCode();
        }
    }

    /*=========================================================================
                                STATIC PART
      =========================================================================*/

    /**
     * Check if every case have been initialize
     */
    private static boolean boardInitialized(ICase[] board) {
        for (ICase area : board)
            if (area == null)
                return false;
        return true;
    }
}
