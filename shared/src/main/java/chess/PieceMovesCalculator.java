package chess;

import java.util.Collection;

abstract class PieceMovesCalculator {

    public final ChessBoard board;
    public final ChessPosition myPosition;
    public final ChessPiece pieceAtStart;
    public Collection<ChessMove> moves = new java.util.ArrayList<>();

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceAtStart = board.getPiece(myPosition);
        pieceMoves();
    }

    public abstract void pieceMoves();

    public boolean validMove(ChessPosition newPosition) {
        // Check if end position is on the board
        if (newPosition.getRow() < 1 || newPosition.getRow() > 8) {
            return false;
        }
        if (newPosition.getColumn() < 1 || newPosition.getColumn() > 8) {
            return false;
        }
        // What pieces are we working with?
        ChessPiece pieceAtEnd = board.getPiece(newPosition);
        // If there is no piece at the end position, it is a valid move
        if (pieceAtEnd == null) {
            return true;
        }
        // If the piece at the end is of the same type as the piece at the start, not a valid move
        if (pieceAtStart.getTeamColor() == pieceAtEnd.getTeamColor()) {
            return false;
        }
        // Otherwise, it is a valid move. We will check for capture on a case by case basis
        return true;
    }

    public boolean isCapture(ChessPosition newPosition) {
        // Get the piece at the end
        ChessPiece pieceAtEnd = board.getPiece(newPosition);
        // Check if there is a piece at the end
        if (pieceAtEnd == null) {
            return false;
        }
        // If the pieces are from different teams, then it is a capture
        if (pieceAtStart.getTeamColor() != pieceAtEnd.getTeamColor()) {
            return true;
        }
        // Otherwise, it is not a capture
        return false;
    }

    void addMove(ChessPosition newPosition, ChessPiece.PieceType promotionPiece) {
        this.moves.add(new ChessMove(myPosition, newPosition.copy(), promotionPiece));
    }
}
