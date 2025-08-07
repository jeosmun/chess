package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class DisplayBoard {

    public static String getWhiteBoard(ChessBoard board) {
        StringBuilder s = new StringBuilder();
        s.append("Current board\n");
        s.append(getHeaderAscending());
        int[] descendingRowOrder = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        for (int i : descendingRowOrder) {
            s.append(getRowAscending(board, i));
        }
        s.append(getHeaderAscending());
        return s.toString();
    }

    public static String getBlackBoard(ChessBoard board) {
        StringBuilder s = new StringBuilder();
        s.append("Current board\n");
        s.append(getHeaderDescending());
        int[] ascendingRowOrder = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        for (int i : ascendingRowOrder) {
            s.append(getRowDescending(board, i));
        }
        s.append(getHeaderDescending());
        return s.toString();
    }

    public static String highlightBoard(ChessBoard board, Collection<ChessMove> moves, ChessGame.TeamColor color) {
        int[] rowOrder;
        int[] colOrder;
        String header;
        if (color == ChessGame.TeamColor.BLACK) {
            rowOrder = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            colOrder = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
            header = getHeaderDescending();
        }
        else {
            rowOrder = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
            colOrder = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            header = getHeaderAscending();
        }
        StringBuilder s = new StringBuilder();
        s.append("Valid Moves\n");
        s.append(header);
        Collection<ChessPosition> endPositions = new ArrayList<>();
        for (ChessMove move : moves) {
            endPositions.add(move.getEndPosition());
        }
        for (int i : rowOrder) {
            s.append(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_LIGHT_GREY);
            s.append("\u2003").append(i).append(" ");
            for (int j : colOrder) {
                if ((i + j) % 2 == 1) {
                    highlightSquare(board, s, endPositions, i, j, SET_BG_COLOR_YELLOW, SET_BG_COLOR_LIGHT_GREY);
                }
                else {
                    highlightSquare(board, s, endPositions, i, j, SET_BG_COLOR_DARK_YELLOW, SET_BG_COLOR_DARK_GREEN);
                }
            }
            s.append(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_LIGHT_GREY);
            s.append("\u2003").append(i).append(" ").append("\n");
            s.append(RESET_BG_COLOR + RESET_TEXT_COLOR);
        }
        s.append(header);
        return s.toString();
    }

    private static void highlightSquare(ChessBoard board, StringBuilder s, Collection<ChessPosition> endPositions,
                                        int i, int j, String highlightColor, String bgColor) {
        ChessPosition position = new ChessPosition(i, j);
        if (endPositions.contains(position)) {
            s.append(highlightColor);
        }
        else {
            s.append(bgColor);
        }
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            s.append(EMPTY);
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            s.append(SET_TEXT_COLOR_WHITE).append(getPieceChar(piece));
        }
        else {
            s.append(SET_TEXT_COLOR_BLACK).append(getPieceChar(piece));
        }
        s.append(RESET_BG_COLOR + RESET_TEXT_COLOR);
    }

    private static String getHeaderAscending() {
        StringBuilder s = new StringBuilder();
        s.append(SET_BG_COLOR_DARK_GREY);
        s.append(EMPTY);
        s.append(SET_TEXT_COLOR_LIGHT_GREY);
        char[] headers = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for (char c : headers) {
            s.append("\u2003").append(c).append(" ");
        }
        s.append(EMPTY);
        s.append(RESET_BG_COLOR + RESET_TEXT_COLOR);
        s.append("\n");
        return s.toString();
    }

    private static String getHeaderDescending() {
        StringBuilder s = new StringBuilder();
        s.append(SET_BG_COLOR_DARK_GREY);
        s.append(EMPTY);
        s.append(SET_TEXT_COLOR_LIGHT_GREY);
        char[] headers = new char[]{'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A'};
        for (char c : headers) {
            s.append("\u2003").append(c).append(" ");
        }
        s.append(EMPTY);
        s.append(RESET_BG_COLOR + RESET_TEXT_COLOR);
        s.append("\n");
        return s.toString();
    }

    private static String getRowDescending(ChessBoard board, int i) {
        StringBuilder s = new StringBuilder();
        ChessPiece[][] squares = board.getSquares();
        int[] descendingRowOrder = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        s.append(getRow(i, descendingRowOrder, squares));
        return s.toString();
    }

    private static String getRowAscending(ChessBoard board, int i) {
        StringBuilder s = new StringBuilder();
        ChessPiece[][] squares = board.getSquares();
        int[] ascendingRowOrder = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        s.append(getRow(i, ascendingRowOrder, squares));
        return s.toString();
    }

    private static String getRow(int i, int[] rowOrder, ChessPiece[][] squares) {
        StringBuilder s = new StringBuilder();
        s.append(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_LIGHT_GREY);
        s.append("\u2003").append(i).append(" ");
        // If i is even, then we start with a light square
        if (i % 2 == 0) {
            for (int j : rowOrder) {
                s.append(getPiece(squares[i - 1][j - 1], j - 1));
            }
        }
        // Otherwise, we start with a dark square
        else {
            for (int j : rowOrder) {
                s.append(getPiece(squares[i - 1][j - 1], j));
            }
        }
        s.append(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_LIGHT_GREY);
        s.append("\u2003").append(i).append(" ");
        s.append(RESET_BG_COLOR + RESET_TEXT_COLOR).append("\n");
        return s.toString();
    }

    private static String getPiece(ChessPiece piece, int dark) {
        StringBuilder s = new StringBuilder();
        if (piece == null) {
            if (dark % 2 == 0) {
                s.append(SET_BG_COLOR_LIGHT_GREY).append(EMPTY);
            }
            else {
                s.append(SET_BG_COLOR_DARK_GREEN).append(EMPTY);
            }
        }
        else {
            if (dark % 2 == 0) {
                s.append(SET_BG_COLOR_LIGHT_GREY);
            }
            else {
                s.append(SET_BG_COLOR_DARK_GREEN);
            }
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                s.append(SET_TEXT_COLOR_WHITE);
            }
            else {
                s.append(SET_TEXT_COLOR_BLACK);
            }
            s.append(getPieceChar(piece));
            s.append(RESET_BG_COLOR + RESET_TEXT_COLOR);
        }
        return s.toString();
    }

    private static String getPieceChar(ChessPiece piece) {
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();
        if (color == ChessGame.TeamColor.WHITE) {
            switch (type) {
                case ChessPiece.PieceType.ROOK -> {
                    return WHITE_ROOK;
                }
                case ChessPiece.PieceType.BISHOP -> {
                    return WHITE_BISHOP;
                }
                case ChessPiece.PieceType.KNIGHT -> {
                    return WHITE_KNIGHT;
                }
                case ChessPiece.PieceType.QUEEN -> {
                    return WHITE_QUEEN;
                }
                case ChessPiece.PieceType.KING -> {
                    return WHITE_KING;
                }
                case ChessPiece.PieceType.PAWN -> {
                    return WHITE_PAWN;
                }
            }
        }
        else {
            if (Objects.requireNonNull(type) == ChessPiece.PieceType.ROOK) {
                return BLACK_ROOK;
            } else if (type == ChessPiece.PieceType.BISHOP) {
                return BLACK_BISHOP;
            } else if (type == ChessPiece.PieceType.KNIGHT) {
                return BLACK_KNIGHT;
            } else if (type == ChessPiece.PieceType.QUEEN) {
                return BLACK_QUEEN;
            } else if (type == ChessPiece.PieceType.KING) {
                return BLACK_KING;
            } else if (type == ChessPiece.PieceType.PAWN) {
                return BLACK_PAWN;
            }
        }
        throw new IllegalStateException("Piece has no color or no type");
    }
}
