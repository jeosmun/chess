package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.State.*;

public class GameClient {
    public final ServerFacade server;
    public final Repl repl;
    public GameData gameData;
    public ChessGame.TeamColor color;

    public GameClient(ServerFacade server, Repl repl) {
        this.server = server;
        this.repl = repl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "redraw" -> redraw();
                case "highlight" -> highlight(params);
                default -> help();
            };
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String leave() throws IOException {
        server.leave(gameData.gameID());
        repl.setState(SIGNEDIN);
        return "Leaving game menu";
    }

    public String move(String[] params) throws ResponseException, IOException {
        if ((params.length == 2 || params.length == 3) && params[0].length() == 2 && params[1].length() == 2) {
            int startColumn = getColumn(params[0].charAt(0));
            int startRow = getRow(params[0].charAt(1));
            ChessPosition startPosition = new ChessPosition(startRow, startColumn);
            int endColumn = getColumn(params[1].charAt(0));
            int endRow = getRow(params[1].charAt(1));
            ChessPosition endPosition = new ChessPosition(endRow, endColumn);
            ChessPiece.PieceType promotionPiece = null;
            if (params.length == 3) {
                if (getPromotionPiece(params[2]) != null) {
                    promotionPiece = getPromotionPiece(params[2]);
                }
                else {
                    throw new ResponseException(400,
                            "move <start column and row> <end column and row> <optional: promotion piece>");
                }
            }
            ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);
            server.makeMove(gameData.gameID(), move);
        }
        else {
            throw new ResponseException(400,
                    "move <start column and row> <end column and row> <optional: promotion piece>");
        }
        return "Move sent to server";
    }

    public String resign() throws IOException {
        server.resign(gameData.gameID());
        return "Sending request to server";
    }

    public String redraw()  {
        if (color == ChessGame.TeamColor.BLACK) {
            return DisplayBoard.getBlackBoard(gameData.game().getBoard());
        }
        return DisplayBoard.getWhiteBoard(gameData.game().getBoard());
    }

    public String highlight(String[] params) throws ResponseException {
        if (params.length == 1) {
            int row = getRow(params[0].charAt(1));
            int col = getColumn(params[0].charAt(0));
            ChessGame game = gameData.game();
            ChessPosition position = new ChessPosition(row, col);
            if (game.getBoard().getPiece(position) == null) {
                return "There is no piece at that position";
            }
            Collection<ChessMove> moves = game.validMoves(position);
            if (color == ChessGame.TeamColor.BLACK) {
                return DisplayBoard.highlightBoard(game.getBoard(), moves, color);
            }
            return DisplayBoard.highlightBoard(game.getBoard(), moves, ChessGame.TeamColor.WHITE);
        }
        else {
            throw new ResponseException(400, "highlight <column and row>");
        }
    }

    public String help() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("leave");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to be removed from game\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("move <start column and row> <end column and row> <optional: promotion piece>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to make a chess move; ; Example: move a7 a8 queen\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("resign");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to forfeit the game\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("redraw");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to redraw the chess board\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("highlight <column and row>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to highlight legal moves for a piece\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("help");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to show possible commands");
        return stringBuilder.toString();
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
        if (Objects.equals(server.getUsername(), gameData.blackUsername())) {
            this.color = ChessGame.TeamColor.BLACK;
        }
        else if (Objects.equals(server.getUsername(), gameData.whiteUsername())) {
            this.color = ChessGame.TeamColor.WHITE;
        }
        else {
            this.color = null;
        }
    }

    public String loadBoard() {
        if (color == ChessGame.TeamColor.BLACK) {
            return DisplayBoard.getBlackBoard(gameData.game().getBoard());
        }
        return DisplayBoard.getWhiteBoard(gameData.game().getBoard());
    }

    public String highlightBoard() {
        return "";
    }

    private int getColumn(char c) throws ResponseException {
        switch (c) {
            case 'a' -> {return 1;}
            case 'b' ->  {return 2;}
            case 'c' -> {return 3;}
            case 'd' -> {return 4;}
            case 'e' -> {return 5;}
            case 'f' -> {return 6;}
            case 'g' -> {return 7;}
            case 'h' -> {return 8;}
            default -> throw new ResponseException(400, "Invalid position <column and row>");
        }
    }

    private int getRow(char c) throws ResponseException {
        switch (c) {
            case '1' -> {return 1;}
            case '2' ->  {return 2;}
            case '3' -> {return 3;}
            case '4' -> {return 4;}
            case '5' -> {return 5;}
            case '6' -> {return 6;}
            case '7' -> {return 7;}
            case '8' -> {return 8;}
            default -> throw new ResponseException(400, "Invalid position <column and row>");
        }
    }

    private ChessPiece.PieceType getPromotionPiece(String piece) {
        switch (piece) {
            case "QUEEN" -> {return ChessPiece.PieceType.QUEEN;}
            case "ROOK" -> {return ChessPiece.PieceType.ROOK;}
            case "KNIGHT" -> {return ChessPiece.PieceType.KNIGHT;}
            case "BISHOP" -> {return ChessPiece.PieceType.BISHOP;}
            default -> {return null;}
        }
    }
}
