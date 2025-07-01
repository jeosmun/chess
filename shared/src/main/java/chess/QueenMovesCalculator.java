package chess;

import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator{

    public QueenMovesCalculator() {}

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new java.util.ArrayList<>();
        ChessPosition newPosition = myPosition.copy();
        // Check up and to the left
        newPosition.updatePosition(1, -1);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(1, -1);
        }
        // Check up
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(1, 0);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(1, 0);
        }
        // Check up and to the right
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(1, 1);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(1, 1);
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
        // Check down and to the left
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(-1, -1);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(-1, -1);
        }
        // Check down
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(-1, 0);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(-1, 0);
        }
        // Check down and to the right
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(-1, 1);
        while(this.validMove(board, myPosition, newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            if (isCapture(board, myPosition, newPosition)) {
                break;
            }
            newPosition.updatePosition(-1, 1);
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
