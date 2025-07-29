package ui;

import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PreloginClient {
    private final ServerFacade server;

    public PreloginClient(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                default -> help();
            };
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("register <USERNAME> <PASSWORD> <EMAIL>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to create an account\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("login <USERNAME> <PASSWORD>");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to play chess\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("quit");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to exit the program\n");
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("help");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to show possible commands");
        return stringBuilder.toString();
    }
}
