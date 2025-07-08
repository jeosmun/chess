package chess;

public class BishopMovesCalculator extends PieceMovesCalculator{

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public void pieceMoves() {
        // Initialize newPosition
        ChessPosition newPosition = myPosition.copy();
        // Check up and to the left
        newPosition.update(1, -1);
        while (validMove(newPosition)) {
            addMove(newPosition, null);
            if (isCapture(newPosition)) {
                break;
            }
            newPosition.update(1, -1);
        }
        // Check up and to the right
        newPosition.update(myPosition);
        newPosition.update(1, 1);
        while (validMove(newPosition)) {
            addMove(newPosition, null);
            if (isCapture(newPosition)) {
                break;
            }
            newPosition.update(1, 1);
        }
        // Check down and to the right
        newPosition.update(myPosition);
        newPosition.update(-1, 1);
        while (validMove(newPosition)) {
            addMove(newPosition, null);
            if (isCapture(newPosition)) {
                break;
            }
            newPosition.update(-1, 1);
        }
        // Check down and to the left
        newPosition.update(myPosition);
        newPosition.update(-1, -1);
        while (validMove(newPosition)) {
            addMove(newPosition, null);
            if (isCapture(newPosition)) {
                break;
            }
            newPosition.update(-1, -1);
        }
    }
}
