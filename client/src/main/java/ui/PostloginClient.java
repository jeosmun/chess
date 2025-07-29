package ui;

import exception.ResponseException;
import model.GameData;
import server.ServerFacade;
import service.results.ListGamesResult;

import java.util.ArrayList;
import java.util.Arrays;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.State.*;

public class PostloginClient {
    private final ServerFacade server;
    private final Repl repl;
    private ArrayList<GameData> gameDataList;

    public PostloginClient(ServerFacade server, Repl repl) {
        this.server = server;
        this.repl = repl;
        this.gameDataList = new ArrayList<>();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "list" -> list();
                default -> help();
            };
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String logout() throws ResponseException {
        server.logout();
        repl.setState(SIGNEDOUT);
        return "successfully logged out";
    }

    public String list() throws ResponseException {
        ListGamesResult res = server.list();
        gameDataList.clear();
        gameDataList.addAll(res.games());
        if (gameDataList.isEmpty()) {
            return "There are no current games";
        }
        StringBuilder result = new StringBuilder();
        GameData game = null;
        boolean first = true;
        for (int i = 0; i < gameDataList.size(); i++) {
            if (first) {
                first = false;
            }
            else {
                result.append("\n");
            }
            game = gameDataList.get(i);
            String name = game.gameName();
            String white = game.whiteUsername();
            String black = game.blackUsername();
            if (white == null) {
                white = "empty";
            }
            if (black == null) {
                black = "empty";
            }
            result.append(String.format("%d.\t Game name: %s\t White: %s\t Black: %s", i, name, white, black));
        }
        return result.toString();
    }

    public String help() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("list");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to list current games\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("create <GAMENAME>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to create a new game\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("join <GAMEID> <COLOR>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to join a game\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("watch <GAMEID>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to watch a game\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("logout");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to logout and return to welcome screen\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("help");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to show possible commands");
        return stringBuilder.toString();
    }
}
