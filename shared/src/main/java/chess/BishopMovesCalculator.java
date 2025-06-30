package chess;

import java.util.Collection;

class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator() {};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return new java.util.ArrayList<>();
    };
}