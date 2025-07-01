package chess;

import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    public PawnMovesCalculator() {}

    @Override
    public boolean isCapture(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        // What pieces are we working with?
        ChessPiece pieceAtStart = board.getPiece(startPosition);
        ChessPiece pieceAtEnd = board.getPiece(endPosition);
        // Check if there is a piece at endPosition
        if (pieceAtEnd == null || startPosition.getColumn() == endPosition.getColumn()) {
            return false;
        }
        return true;
    }

    @Override
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
            if (startPosition.getColumn() != endPosition.getColumn()) {
                return false;
            }
            return true;
        }
        if (pieceAtStart.getTeamColor() == pieceAtEnd.getTeamColor()) {
            return false;
        }
        // Can't check if it is a capture separately
        if (isCapture( board, startPosition, endPosition)) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new java.util.ArrayList<>();
        ChessPosition newPosition = myPosition.copy();
        // Check color of pawn
        ChessGame.TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();
        // White moves up
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            // Check directly up
            newPosition.updatePosition(1, 0);
            if (validMove(board, myPosition, newPosition)) {
                if (validMove(board, myPosition, newPosition)) {
                    if (newPosition.getRow() == 8) {
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.BISHOP));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, newPosition.copy(), null));
                    }
                }
            }
            // Check if pawn is on start and up 2
            newPosition.updatePosition(1, 0);
            ChessPosition intermediatePosition = myPosition.copy();
            intermediatePosition.updatePosition(1, 0);
            if (myPosition.getRow() == 2 && validMove(board, myPosition, newPosition)
                    && validMove(board, myPosition, intermediatePosition)) {
                if (validMove(board, myPosition, newPosition)) {
                    if (newPosition.getRow() == 8) {
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.BISHOP));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, newPosition.copy(), null));
                    }
                }
            }
            newPosition.updatePosition(myPosition);
            // Check if up left is capture
            newPosition.updatePosition(1, -1);
            if (validMove(board, myPosition, newPosition)) {
                if (newPosition.getRow() == 8) {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.BISHOP));
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), null));
                }
            }
            newPosition.updatePosition(myPosition);
            // Check if up right is capture
            newPosition.updatePosition(1, 1);
            if (validMove(board, myPosition, newPosition)) {
                if (newPosition.getRow() == 8) {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.BISHOP));
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), null));
                }
            }
        }
        // Black moves down
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            // Check directly down
            newPosition.updatePosition(-1, 0);
            if (validMove(board, myPosition, newPosition)) {
                if (validMove(board, myPosition, newPosition)) {
                    if (newPosition.getRow() == 1) {
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.BISHOP));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, newPosition.copy(), null));
                    }
                }
            }
            // Check if pawn is on start and up 2
            newPosition.updatePosition(-1, 0);
            ChessPosition intermediatePosition = myPosition.copy();
            intermediatePosition.updatePosition(-1, 0);
            if (myPosition.getRow() == 7 && validMove(board, myPosition, newPosition)
                    && validMove(board, myPosition, intermediatePosition)) {
                if (newPosition.getRow() == 1) {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.BISHOP));
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), null));
                }
            }
            newPosition.updatePosition(myPosition);
            // Check if down left is capture
            newPosition.updatePosition(-1, -1);
            if (validMove(board, myPosition, newPosition)) {
                if (newPosition.getRow() == 1) {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.BISHOP));
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), null));
                }
            }
            newPosition.updatePosition(myPosition);
            // Check if down right is capture
            newPosition.updatePosition(-1, 1);
            if (validMove(board, myPosition, newPosition)) {
                if (newPosition.getRow() == 1) {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition.copy(), ChessPiece.PieceType.BISHOP));
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition.copy(), null));
                }
            }
        }
        return moves;
    }
}
