package chess;

import java.util.Collection;

abstract class PieceMovesCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public boolean isCapture(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        // What pieces are we working with?
        ChessPiece pieceAtStart = board.getPiece(startPosition);
        ChessPiece pieceAtEnd = board.getPiece(endPosition);
        // Check if there is a piece at endPosition
        if (pieceAtEnd == null) {
            return false;
        }
        return true;
    }

    public boolean validMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        // Check if position is on the board
        if (endPosition.getRow() < 1 || endPosition.getRow() > 8) {
            return false;
        }
        if (endPosition.getColumn() < 1 || endPosition.getColumn() > 8) {
            return false;
        }
        // What pieces are we working with?
        ChessPiece pieceAtStart = board.getPiece(startPosition);
        ChessPiece pieceAtEnd = board.getPiece(endPosition);
        // Check if there is a piece at endPosition
        if (pieceAtEnd == null) {
            return true;
        }
        // Check if position is occupied by another piece of your team
        if (pieceAtStart.getTeamColor() == pieceAtEnd.getTeamColor()) {
            return false;
        }
        // Otherwise we are good! We will check if it is a capture separately.
        return true;
    }
}
