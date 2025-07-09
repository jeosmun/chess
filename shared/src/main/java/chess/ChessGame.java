package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    private ChessPosition whiteKingPosition;
    private ChessPosition blackKingPosition;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
        whiteKingPosition = new ChessPosition(1, 5);
        blackKingPosition = new ChessPosition(8, 5);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Check if move is null
        if(move == null) {
            throw new InvalidMoveException();
        }
        // Check if move is a valid move
//        Collection<ChessMove> moves = validMoves(move.getStartPosition());
//        if(!moves.contains(move)) {
//            throw new InvalidMoveException();
//        }
        // Try to execute the move, catch errors if they appear
        try {
            applyMove(move);
        }
        catch(Exception ex){
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Check for threats from Queens and Rooks
        // Check for threats from Queens and Bishops
        // Check for threats from Pawns
        // Check for threats from Knights
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        whiteKingPosition = null;
        blackKingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() == TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    whiteKingPosition = new ChessPosition(i, j);
                }
                if (piece.getTeamColor() == TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    blackKingPosition = new ChessPosition(i, j);
                }
            }
        }
        if (whiteKingPosition == null || blackKingPosition == null) {
            throw new IllegalStateException("Missing black king or white king or both");
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private void applyMove(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        ChessPiece startPiece = board.getPiece(startPosition);
        if(promotionPiece != null) {
            board.addPiece(endPosition, new ChessPiece(getTeamTurn(), promotionPiece));
            board.addPiece(startPosition, null);
        }
        else {
            board.addPiece(endPosition, board.getPiece(startPosition));
            board.addPiece(startPosition, null);
        }
        // Check if the king positions need to be updated
        if(startPiece.getPieceType() == ChessPiece.PieceType.KING) {
            if(startPiece.getTeamColor() == TeamColor.WHITE) {
                whiteKingPosition.update(endPosition);
            }
            if(startPiece.getTeamColor() == TeamColor.BLACK) {
                blackKingPosition.update(endPosition);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

//    @Override
//    public String toString() {
//        return "ChessGame{" +
//                "board=" + board +
//                ", teamTurn=" + teamTurn +
//                ", whiteKingPosition=" + whiteKingPosition +
//                ", blackKingPosition=" + blackKingPosition +
//                '}';
//    }
}
