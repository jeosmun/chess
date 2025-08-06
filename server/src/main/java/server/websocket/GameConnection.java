package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class GameConnection {
    public ConcurrentHashMap<String, Connection> observers;
    public Connection whitePlayer;
    public Connection blackPlayer;
    public int gameID;

    public GameConnection(int gameID) {
        this.gameID = gameID;
    }

    public void addObserver(String username, Session session) {
        var connection = new Connection(username, session);
        observers.put(username, connection);
    }

    public void removeObserver(String username) {
        observers.remove(username);
    }

    public void setWhitePlayer(String username, Session session) {
        this.whitePlayer = new Connection(username, session);
    }

    public void setBlackPlayer(String username, Session session) {
        this.blackPlayer = new Connection(username, session);
    }

    public void removeWhitePlayer() {
        this.whitePlayer = null;
    }

    public void removeBlackPlayer() {
        this.blackPlayer = null;
    }
}
