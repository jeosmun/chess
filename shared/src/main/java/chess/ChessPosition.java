package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int column;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return column;
    }

    public void update(int changeRow, int changeColumn) {
        this.row = row + changeRow;
        this.column = column + changeColumn;
    }

    public void update(ChessPosition position) {
        this.row = position.getRow();
        this.column = position.getColumn();
    }

    public ChessPosition copy() {
        return new ChessPosition(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public String toString() {
        var positionString = new StringBuilder();
        positionString.append("(");
        positionString.append(row);
        positionString.append(", ");
        positionString.append(column);
        positionString.append(")");
        return positionString.toString();
    }
}
