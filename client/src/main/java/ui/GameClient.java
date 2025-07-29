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
                case "exit" -> exit();
                default -> help();
            };
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String exit() {
        repl.setState(SIGNEDIN);
        return "Exiting game menu";
    }

    public String help() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append("exit");
        stringBuilder.append(RESET_TEXT_COLOR);
        stringBuilder.append(" - to exit game menu");
        return stringBuilder.toString();
    }
}
