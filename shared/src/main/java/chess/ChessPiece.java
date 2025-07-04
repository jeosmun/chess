package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new java.util.ArrayList<>();
        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            BishopMovesCalculator calc = new BishopMovesCalculator();
            moves = calc.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            KingMovesCalculator calc = new KingMovesCalculator();
            moves = calc.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            KnightMovesCalculator calc = new KnightMovesCalculator();
            moves = calc.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            PawnMovesCalculator calc = new PawnMovesCalculator();
            moves = calc.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            QueenMovesCalculator calc = new QueenMovesCalculator();
            moves = calc.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            RookMovesCalculator calc = new RookMovesCalculator();
            moves = calc.pieceMoves(board, myPosition);
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
