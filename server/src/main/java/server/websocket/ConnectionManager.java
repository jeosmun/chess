package server.websocket;

import websocket.commands.ConnectCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, GameConnection> gameConnections = new ConcurrentHashMap<>();

    public void add(int gameID, Connection connection, ConnectCommand.UserType userType) {
        if (gameConnections.get(gameID) == null) {
            gameConnections.put(gameID, new GameConnection(gameID));
        }
        if (userType == ConnectCommand.UserType.WHITE_PLAYER) {
            gameConnections.get(gameID).setWhitePlayer(connection);
        }
        else if (userType == ConnectCommand.UserType.BLACK_PLAYER) {
            gameConnections.get(gameID).setBlackPlayer(connection);
        }
        else if (userType == ConnectCommand.UserType.OBSERVER) {
            gameConnections.get(gameID).addObserver(connection);
        }
    }

    public GameConnection get(int gameID) {
        return gameConnections.get(gameID);
    }

    public void broadcast(int gameID, String excludeToken, ServerMessage message) throws IOException {
        GameConnection gameConnection = gameConnections.get(gameID);
        Connection whiteConnection = gameConnection.getWhiteConnection();
        Connection blackConnection = gameConnection.getBlackConnection();
        if (whiteConnection != null && whiteConnection.getSession().isOpen()) {
            if (!Objects.equals(excludeToken, whiteConnection.getAuthToken())) {
                whiteConnection.send(message.toString());
            }
        }
        else {
            if (whiteConnection != null) {
                gameConnection.removeWhitePlayer();
            }
        }
        if (blackConnection != null && blackConnection.getSession().isOpen()) {
            if (!Objects.equals(excludeToken, blackConnection.getAuthToken())) {
                blackConnection.send(message.toString());
            }
        }
        else {
            if (blackConnection != null) {
                gameConnection.removeBlackPlayer();
            }
        }
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection c : gameConnection.getObservers().values()) {
            if (c.getSession().isOpen()) {
                if (!Objects.equals(excludeToken, c.getAuthToken())) {
                    c.send(message.toString());
                }
            }
            else {removeList.add(c);}
        }
        for (Connection c : removeList) {
            gameConnection.removeObserver(c.getAuthToken());
        }
    }
}
