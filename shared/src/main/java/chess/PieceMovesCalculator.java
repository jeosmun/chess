package chess;

import java.util.Collection;

abstract class PieceMovesCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public boolean validMove(ChessPosition position) {
        // Check if position is on the board
        if (position.getRow() < 1 || position.getRow() > 8) {
            return false;
        }
        if (position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }
        // Check if position is occupied by another piece of your team
        // Check if position is occupied by another piece of opponent's team
        return true;
    }
}
