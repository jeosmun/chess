package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class GameConnection {
    private final ConcurrentHashMap<String, Connection> observers = new ConcurrentHashMap<>();
    private Connection whitePlayer;
    private Connection blackPlayer;
    private int gameID;

    public GameConnection(int gameID) {
        this.gameID = gameID;
    }

    public void addObserver(Connection connection) {
        observers.put(connection.getAuthToken(), connection);
    }

    public void removeObserver(String authToken) {
        Connection c = observers.get(authToken);
        c.closeSession();
        observers.remove(authToken);
    }

    public ConcurrentHashMap<String, Connection> getObservers() {
        return observers;
    }

    public void setWhitePlayer(Connection connection) {
        this.whitePlayer = connection;
    }

    public void setBlackPlayer(Connection connection) {
        this.blackPlayer = connection;
    }

    public Connection getWhiteConnection() {
        return whitePlayer;
    }

    public Connection getBlackConnection() {
        return blackPlayer;
    }

    public void removeWhitePlayer() {
        whitePlayer.closeSession();
        this.whitePlayer = null;
    }

    public void removeBlackPlayer() {
        blackPlayer.closeSession();
        this.blackPlayer = null;
    }
}
