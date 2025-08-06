package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final GameService gameService;

    public WebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect((ConnectCommand) command, session);
            case MAKE_MOVE -> makeMove((MoveCommand) command, session);
            case LEAVE -> leave((LeaveCommand) command, session);
            case RESIGN -> resign((ResignCommand) command, session);
        }
    }

    public void connect(ConnectCommand command, Session session) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        ConnectCommand.UserType userType = command.getUserType();
        String rootToken = command.getAuthToken();
        Connection connection = new Connection(rootToken, session);
        connectionManager.add(gameID, connection, userType);
        // Send a load game back to the root user
        connection.send(new Gson().toJson(new LoadGameMessage(gameService.getGameData(gameID))));
        // Send a broadcast message to everyone else
        String playerType = null;
        switch (userType) {
            case WHITE_PLAYER -> playerType = "white";
            case BLACK_PLAYER -> playerType = "black";
            case OBSERVER -> playerType = "observer";
        }
        String text = String.format("%s has joined the game as %s", command.getUsername(), playerType);
        NotificationMessage message = new NotificationMessage(text);
        connectionManager.broadcast(gameID, rootToken, message);
    }

    public void makeMove(MoveCommand command, Session session) {

    }

    public void leave(LeaveCommand command, Session session) {

    }

    public void resign(ResignCommand command, Session session) {

    }
}
