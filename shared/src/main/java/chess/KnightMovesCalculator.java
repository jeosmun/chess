package chess;

public class KnightMovesCalculator extends PieceMovesCalculator{

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition) {super(board, myPosition);}

    public void pieceMoves() {
        // Initialize newPosition
        ChessPosition newPosition = myPosition.copy();
        // Check up 2 left 1
        newPosition.update(2, -1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check up 2 right 1
        newPosition.update(myPosition);
        newPosition.update(2, 1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check up 1 right 2
        newPosition.update(myPosition);
        newPosition.update(1, 2);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check down 1 right 2
        newPosition.update(myPosition);
        newPosition.update(-1, 2);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check down 2 right 1
        newPosition.update(myPosition);
        newPosition.update(-2, 1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check down 2 left 1
        newPosition.update(myPosition);
        newPosition.update(-2, -1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check down 1 left 2
        newPosition.update(myPosition);
        newPosition.update(-1, -2);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check up 1 left 2
        newPosition.update(myPosition);
        newPosition.update(1, -2);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
    }
}
