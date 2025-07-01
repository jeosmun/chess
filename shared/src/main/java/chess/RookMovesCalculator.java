package chess;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator{

    public RookMovesCalculator() {}

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new java.util.ArrayList<>();
        ChessPosition newPosition = myPosition.copy();
        // Check up
        newPosition.updatePosition(1, 0);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(1, 0);
        }
        // Check right
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(0, 1);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(0, 1);
        }
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(-1, 0);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(-1, 0);
        }
        // Check left
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(0, -1);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(0, -1);
        }
        return moves;
    }
}
