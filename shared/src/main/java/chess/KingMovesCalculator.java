package chess;

public class KingMovesCalculator extends PieceMovesCalculator{

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {super(board, myPosition);}

    public void pieceMoves() {
        // Initialize newPosition
        ChessPosition newPosition = myPosition.copy();
        // Check up and left
        newPosition.update(1, -1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check up
        newPosition.update(0, 1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check up and right
        newPosition.update(0, 1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check right
        newPosition.update(-1, 0);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check down and right
        newPosition.update(-1, 0);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check down
        newPosition.update(0, -1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check down and left
        newPosition.update(0, -1);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
        // Check left
        newPosition.update(1, 0);
        if (validMove(newPosition)) {
            addMove(newPosition, null);
        }
    }
}
