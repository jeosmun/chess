package chess;

import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator{

    public KingMovesCalculator() {}

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new java.util.ArrayList<>();
        ChessPosition newPosition = myPosition.copy();
        // Check up and left
        newPosition.updatePosition(1, -1);
        if(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check up
        newPosition.updatePosition(1, 0);
        if(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check up and right
        newPosition.updatePosition(1, 1);
        if(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check right
        newPosition.updatePosition(0, 1);
        if(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check down and right
        newPosition.updatePosition(-1, 1);
        if(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check down
        newPosition.updatePosition(-1, 0);
        if(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check down and left
        newPosition.updatePosition(-1, -1);
        if(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        newPosition.updatePosition(myPosition);
        // Check left
        newPosition.updatePosition(0, -1);
        if(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
        }
        return moves;
    }
}
