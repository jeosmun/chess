package server.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, GameConnection> gameConnections = new ConcurrentHashMap<>();

    public void addGameConnection(int gameID) {
        gameConnections.put(gameID, new GameConnection(gameID));
    }

    public void removeGameConnection(int gameID) {
        gameConnections.remove(gameID);
    }
}
