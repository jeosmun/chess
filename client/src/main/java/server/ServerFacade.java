package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import requests.*;
import results.CreateGameResult;
import results.ListGamesResult;
import results.LoginResult;
import results.RegisterResult;
import server.websocket.NotificationHandler;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.websocket.*;

public class ServerFacade extends Endpoint {

    private final String serverUrl;
    private String authToken = null;
    private String username = null;
    // For the websocket connection
    public Session session;
    public UserGameCommand.UserType userType;
    public NotificationHandler notificationHandler;

    public ServerFacade(String url, NotificationHandler notificationHandler) {
        serverUrl = url;
        this.notificationHandler = notificationHandler;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        RegisterResult res = this.makeRequest("POST", path, request, RegisterResult.class);
        this.username = res.username();
        // Possibility for trouble here if no authToken is returned.
        this.authToken = res.authToken();
        return res;
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        LoginResult res = this.makeRequest("POST", path, request, LoginResult.class);
        this.username = res.username();
        // Possibility for trouble here if no authToken is returned.
        this.authToken = res.authToken();
        return res;
    }

    public void logout() throws ResponseException {
        var path = "/session";
        LogoutRequest request = new LogoutRequest(authToken);
        this.makeRequest("DELETE", path, request, null);
        this.username = null;
        // May be able to set authToken to null even if it fails?
        this.authToken = null;
    }

    public ListGamesResult list() throws ResponseException {
        var path = "/game";
        ListGamesRequest request = new ListGamesRequest(authToken);
        return this.makeRequest("GET", path, null, ListGamesResult.class);
    }

    public CreateGameResult create(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public void join(JoinGameRequest request) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, request, null);
    }

    public void clear() throws ResponseException {
        this.authToken = null;
        this.username = null;
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request,
                            Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        }
        catch (ResponseException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream resBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(resBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {return status >= 200 && status < 300;}

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream resErr = http.getErrorStream()) {
                if (resErr != null) {
                    throw ResponseException.fromJson(status, resErr);
                }
            }
            throw new ResponseException(status, "other failure: " + status);
        }
    }

    public String getUsername() {return username;}

    public String getAuthToken() {return authToken;}

    // Here is my code for the websocket

    public void connectWS(String color, int gameID) throws ResponseException {
        try {
            var url = serverUrl.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> {
                            notificationHandler.notify(new Gson().fromJson(message, NotificationMessage.class));
                        }
                        case LOAD_GAME -> {
                            notificationHandler.notify(new Gson().fromJson(message, LoadGameMessage.class));
                        }
                        case ERROR -> {
                            notificationHandler.notify(new Gson().fromJson(message, ErrorMessage.class));
                        }
                    }
                }
            });
            ConnectCommand.UserType userType;
            switch (color) {
                case "WHITE" -> userType = ConnectCommand.UserType.WHITE_PLAYER;
                case "BLACK" -> userType = ConnectCommand.UserType.BLACK_PLAYER;
                case null, default -> userType = UserGameCommand.UserType.OBSERVER;
            }
            this.userType = userType;
            ConnectCommand command = new ConnectCommand(userType, this.username, this.authToken, gameID);
            send(new Gson().toJson(command));
        }
        catch (URISyntaxException | IOException | DeploymentException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(int gameID) throws IOException {
        LeaveCommand command = new LeaveCommand(this.userType, this.username, this.authToken, gameID);
        send(new Gson().toJson(command));
    }

    public void resign(int gameID) throws IOException {
        ResignCommand command = new ResignCommand(this.userType, this.username, this.authToken, gameID);
        send(new Gson().toJson(command));
    }

    public void makeMove(int gameID, ChessMove move) throws IOException {
        MoveCommand command = new MoveCommand(this.userType, this.username, this.authToken, gameID, move);
        send(new Gson().toJson(command));
    }

    private void send(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // Endpoint requires this method, but nothing has to be done
    }
}
