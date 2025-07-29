package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class DisplayBoard {
    // Board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    // Padded characters

    public static String printBoardString(ChessBoard board) {
        ChessPiece[][] squares = board.getSquares();
        StringBuilder result = new StringBuilder();
        for (ChessPiece[] row : squares) {
            for (ChessPiece square : row) {
                if (square != null) {
                    result.append(square.toString() + ", ");
                }
                else {
                    result.append(" ");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static void printBoard(ChessBoard board) {
        // Do it with white on bottom first
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println("From white perspective: ");
        printHeaderAscending(out);
        int[] descendingRowOrder = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        for (int i : descendingRowOrder) {
            printRowAscending(out, board, i);
        }
        printHeaderAscending(out);
        // Now do it with black on bottom
        out.println("From black perspective: ");
        printHeaderDescending(out);
        int[] ascendingRowOrder = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        for (int i : ascendingRowOrder) {
            printRowDescending(out, board, i);
        }
        printHeaderDescending(out);
    }

    private static void printRowDescending(PrintStream out, ChessBoard board, int i) {
        ChessPiece[][] squares = board.getSquares();
        int[] descendingRowOrder = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        printRow(out, i, descendingRowOrder, squares);
    }

    private static void printHeaderDescending(PrintStream out) {
        setDarkGrey(out);
        out.print(EMPTY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        char[] headers = new char[]{'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A'};
        for (char c : headers) {
            out.print("\u2003" + c + " ");
        }
        out.print(EMPTY);
        reset(out);
        out.print("\n");
    }

    private static void printRowAscending(PrintStream out, ChessBoard board, int i) {
        ChessPiece[][] squares = board.getSquares();
        int[] ascendingRowOrder = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        printRow(out, i, ascendingRowOrder, squares);
//        setDarkGrey(out);
//        out.print(SET_TEXT_COLOR_LIGHT_GREY);
//        out.print("\u2003" + i + " ");
//        // If i is even, then we start with a light square
//        if (i % 2 == 0) {
//            for (int j : ascendingRowOrder) {
//                printPiece(out, squares[i - 1][j - 1], j - 1);
//            }
//        }
//        // Otherwise, we start with a dark square
//        else {
//            for (int j : ascendingRowOrder) {
//                printPiece(out, squares[i - 1][j - 1], j);
//            }
//        }
//        setDarkGrey(out);
//        out.print(SET_TEXT_COLOR_LIGHT_GREY);
//        out.print("\u2003" + i + " ");
//        reset(out);
//        out.print("\n");
    }

    private static void printHeaderAscending(PrintStream out) {
        setDarkGrey(out);
        out.print(EMPTY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        char[] headers = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for (char c : headers) {
            out.print("\u2003" + c + " ");
        }
        out.print(EMPTY);
        reset(out);
        out.print("\n");
    }

    private static void printRow(PrintStream out, int i, int[] rowOrder, ChessPiece[][] squares) {
        setDarkGrey(out);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print("\u2003" + i + " ");
        // If i is even, then we start with a light square
        if (i % 2 == 0) {
            for (int j : rowOrder) {
                printPiece(out, squares[i - 1][j - 1], j - 1);
            }
        }
        // Otherwise, we start with a dark square
        else {
            for (int j : rowOrder) {
                printPiece(out, squares[i - 1][j - 1], j);
            }
        }
        setDarkGrey(out);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print("\u2003" + i + " ");
        reset(out);
        out.print("\n");
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setLightGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setDarkGrey(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
    }

    private static void reset(PrintStream out) {
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
    }

    private static void printPiece(PrintStream out, ChessPiece piece, int dark) {
        if (piece == null) {
            if (dark % 2 == 0) {
                setLightGrey(out);
                out.print(EMPTY);
            }
            else {
                setBlack(out);
                out.print(EMPTY);
            }
        }
        else {
            if (dark % 2 == 0) {
                setLightGrey(out);
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(SET_TEXT_COLOR_WHITE);
                }
                else {
                    out.print(SET_TEXT_COLOR_DARK_GREY);
                }
                out.print(getPieceChar(piece));
            }
            else {
                setBlack(out);
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(SET_TEXT_COLOR_WHITE);
                }
                else {
                    out.print(SET_TEXT_COLOR_DARK_GREY);
                }
                out.print(getPieceChar(piece));
            }
        }
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
