package chess;

public class RookMovesCalculator extends PieceMovesCalculator{

    public RookMovesCalculator(ChessBoard board, ChessPosition myPosition) {super(board, myPosition);}

    public void pieceMoves() {
        // Initialize newPosition
        ChessPosition newPosition = myPosition.copy();
        // Check up
        newPosition.update(myPosition);
        newPosition.update(1, 0);
        while (validMove(newPosition)) {
            addMove(newPosition, null);
            if (isCapture(newPosition)) {
                break;
            }
            newPosition.update(1, 0);
        }
        // Check right
        newPosition.update(myPosition);
        newPosition.update(0, 1);
        while (validMove(newPosition)) {
            addMove(newPosition, null);
            if (isCapture(newPosition)) {
                break;
            }
            newPosition.update(0, 1);
        }
        // Check down
        newPosition.update(myPosition);
        newPosition.update(-1, 0);
        while (validMove(newPosition)) {
            addMove(newPosition, null);
            if (isCapture(newPosition)) {
                break;
            }
            newPosition.update(-1, 0);
        }
        newPosition.update(myPosition);
        newPosition.update(0, -1);
        while (validMove(newPosition)) {
            addMove(newPosition, null);
            if (isCapture(newPosition)) {
                break;
            }
            newPosition.update(0, -1);
        }
    }
}
