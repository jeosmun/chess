package ui;

import exception.ResponseException;
import server.ServerFacade;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import static ui.State.*;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PreloginClient {
    private final ServerFacade server;
    private final Repl repl;

    public PreloginClient(ServerFacade server, Repl repl) {
        this.server = server;
        this.repl = repl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                case "register" -> register(params);
                case "login" -> login(params);
                default -> help();
            };
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            String username = params[0];
            String password = params[1];
            LoginResult res = server.login(new LoginRequest(username, password));
            if (res.authToken() == null || res.username() == null) {
                throw new ResponseException(500, "Error: failed to obtain authentication");
            }
            repl.setState(SIGNEDIN);
            // Need to call this in order to initialize the list of games in the postloginClient
            repl.postloginClient.list();
            return String.format("%s has been logged in", res.username());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterResult res = server.register(new RegisterRequest(username, password, email));
            if (res.authToken() == null || res.username() == null) {
                throw new ResponseException(500, "Error: failed to obtain authentication");
            }
            repl.setState(SIGNEDIN);
            // Need to call this in order to initialize the list of games in the postloginClient
            repl.postloginClient.list();
            return String.format("Congratulations! %s has been registered.", res.username());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
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
