package chess;

import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public KnightMovesCalculator() {}

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new java.util.ArrayList<>();
        ChessPosition newPosition = myPosition.copy();
        // Check up 2 and left 1
        newPosition.updatePosition(2, -1);
        if(validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check up 2 and right 1
        newPosition.updatePosition(2, 1);
        if(validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check right 2 and up 1
        newPosition.updatePosition(1, 2);
        if(validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check right 2 and down 1
        newPosition.updatePosition(-1, 2);
        if(validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check down 2 and right 1
        newPosition.updatePosition(-2, 1);
        if(validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check down 2 and left 1
        newPosition.updatePosition(-2, -1);
        if(validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check left 2 and down 1
        newPosition.updatePosition(-1, -2);
        if(validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check left 2 and up 1
        newPosition.updatePosition(1, -2);
        if(validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        return moves;
    }
}
