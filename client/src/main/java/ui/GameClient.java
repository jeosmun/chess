package ui;

import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.State.*;

public class GameClient {
    public final ServerFacade server;
    public final Repl repl;

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

    public String leave() {
        repl.setState(SIGNEDIN);
        return "Leaving game menu";
    }

    public String move(String[] params) {
        throw new RuntimeException("Not implemented");
    }

    public String resign() {
        throw new RuntimeException("Not implemented");
    }

    public String redraw()  {
        throw new RuntimeException("Not implemented");
    }

    public String highlight(String[] params) {
        throw new RuntimeException("Not implemented");
    }

    public String help() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("leave");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to be removed from game");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("move <start row and column> <end row and column>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to make a chess move; ; Example: move a1 b1");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("resign");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to forfeit the game");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("redraw");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to redraw the chess board");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("highlight <row and column>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to highlight legal moves for a piece");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("help");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to show possible commands");
        return stringBuilder.toString();
    }
}
