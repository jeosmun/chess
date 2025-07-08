package chess;

public class PawnMovesCalculator extends PieceMovesCalculator {

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {super(board, myPosition);}

    public void pieceMoves() {
        // Initialize newPosition
        ChessPosition newPosition = myPosition.copy();
        // Find color of the starting piece
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        // If myColor is white, we are going up
        if (myColor == ChessGame.TeamColor.WHITE) {
            // Check up
            newPosition.update(1, 0);
            if (validMove(newPosition)) {
                // Check Promotions
                if (newPosition.getRow() == 8) {
                    addMove(newPosition, ChessPiece.PieceType.KNIGHT);
                    addMove(newPosition, ChessPiece.PieceType.BISHOP);
                    addMove(newPosition, ChessPiece.PieceType.QUEEN);
                    addMove(newPosition, ChessPiece.PieceType.ROOK);
                }
                else {
                    addMove(newPosition, null);
                }
            }
            // Check if on starting line and previous move was valid
            if (myPosition.getRow() == 2 && validMove(newPosition)) {
                newPosition.update(1, 0);
                if (validMove(newPosition)) {
                    addMove(newPosition, null);
                }
            }
            newPosition.update(myPosition);
            // Check up and left
            newPosition.update(1, -1);
            if (validMove(newPosition)) {
                // Check Promotions
                if (newPosition.getRow() == 8) {
                    addMove(newPosition, ChessPiece.PieceType.KNIGHT);
                    addMove(newPosition, ChessPiece.PieceType.BISHOP);
                    addMove(newPosition, ChessPiece.PieceType.QUEEN);
                    addMove(newPosition, ChessPiece.PieceType.ROOK);
                }
                else {
                    addMove(newPosition, null);
                }
            }
            // Check up and right
            newPosition.update(0, 2);
            if (validMove(newPosition)) {
                // Check Promotions
                if (newPosition.getRow() == 8) {
                    addMove(newPosition, ChessPiece.PieceType.KNIGHT);
                    addMove(newPosition, ChessPiece.PieceType.BISHOP);
                    addMove(newPosition, ChessPiece.PieceType.QUEEN);
                    addMove(newPosition, ChessPiece.PieceType.ROOK);
                }
                else {
                    addMove(newPosition, null);
                }
            }
        }
        // If myColor is black, we are going down
        if (myColor == ChessGame.TeamColor.BLACK) {
            // Check down
            newPosition.update(-1, 0);
            if (validMove(newPosition)) {
                // Check Promotions
                if (newPosition.getRow() == 1) {
                    addMove(newPosition, ChessPiece.PieceType.KNIGHT);
                    addMove(newPosition, ChessPiece.PieceType.BISHOP);
                    addMove(newPosition, ChessPiece.PieceType.QUEEN);
                    addMove(newPosition, ChessPiece.PieceType.ROOK);
                }
                else {
                    addMove(newPosition, null);
                }
            }
            // Check if on starting line and previous move was valid
            if (myPosition.getRow() == 7 && validMove(newPosition)) {
                newPosition.update(-1, 0);
                if (validMove(newPosition)) {
                    addMove(newPosition, null);
                }
            }
            newPosition.update(myPosition);
            // Check down and left
            newPosition.update(-1, -1);
            if (validMove(newPosition)) {
                // Check Promotions
                if (newPosition.getRow() == 1) {
                    addMove(newPosition, ChessPiece.PieceType.KNIGHT);
                    addMove(newPosition, ChessPiece.PieceType.BISHOP);
                    addMove(newPosition, ChessPiece.PieceType.QUEEN);
                    addMove(newPosition, ChessPiece.PieceType.ROOK);
                }
                else {
                    addMove(newPosition, null);
                }
            }
            // Check down and right
            newPosition.update(0, 2);
            if (validMove(newPosition)) {
                // Check Promotions
                if (newPosition.getRow() == 1) {
                    addMove(newPosition, ChessPiece.PieceType.KNIGHT);
                    addMove(newPosition, ChessPiece.PieceType.BISHOP);
                    addMove(newPosition, ChessPiece.PieceType.QUEEN);
                    addMove(newPosition, ChessPiece.PieceType.ROOK);
                }
                else {
                    addMove(newPosition, null);
                }
            }
        }
    }

    @Override
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
        // Are we staying in the same column?
        if (myPosition.getColumn() == newPosition.getColumn() && pieceAtEnd == null) {
            return true;
        }
        // If not the same column, are we capturing anything?
        if (isCapture(newPosition)) {
            return true;
        }
        // Else, it cannot be a valid move
        return false;
    }

    @Override
    public boolean isCapture(ChessPosition newPosition) {
        // What pieces are we working with?
        ChessPiece pieceAtEnd = board.getPiece(newPosition);
        // If there is no piece to capture, false
        if (pieceAtEnd == null) {
            return false;
        }
        // What colors are we working with?
        ChessGame.TeamColor startColor = pieceAtStart.getTeamColor();
        ChessGame.TeamColor endColor = pieceAtEnd.getTeamColor();
        // If the pieces are the same color or the column is the same, not a capture
        if (startColor == endColor || myPosition.getColumn() == newPosition.getColumn()) {
            return false;
        }
        // Otherwise it's good
        return true;
    }
}
