package chess;

import java.util.Collection;

class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator() {}

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new java.util.ArrayList<>();
        // Check up and to the left
        ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        newPosition.updatePosition(1, -1);
        while(this.validMove(newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            newPosition.updatePosition(1, -1);
        }
        // Check up and to the right
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(1, 1);
        while(this.validMove(newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            newPosition.updatePosition(1, 1);
        }
        // Check down and to the left
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(-1, -1);
        while(this.validMove(newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            newPosition.updatePosition(-1, -1);
        }
        // Check down and to the right
        newPosition.updatePosition(myPosition);
        newPosition.updatePosition(-1, 1);
        while(this.validMove(newPosition)) {
            moves.add(new ChessMove(myPosition, newPosition.copy(), null));
            newPosition.updatePosition(-1, 1);
        }
        return moves;
    }
}