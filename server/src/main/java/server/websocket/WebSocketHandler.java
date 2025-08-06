package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
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
        // Maybe we need to catch exceptions here and print them out to the server terminal?
        switch (command.getCommandType()) {
            case CONNECT -> connect((ConnectCommand) command, session);
            case MAKE_MOVE -> makeMove((MoveCommand) command, session);
            case LEAVE -> leave((LeaveCommand) command, session);
            case RESIGN -> resign((ResignCommand) command, session);
        }
    }

    public void connect(ConnectCommand command, Session session) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        UserGameCommand.UserType userType = command.getUserType();
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

    public void makeMove(MoveCommand command, Session session) throws DataAccessException, IOException {
        // Get relevant game data
        int gameID = command.getGameID();
        GameData gameData = gameService.getGameData(gameID);
        ChessGame game = gameData.game();
        ChessMove move = command.getMove();
        // Check if move is valid (also, what happens if there is no piece at the startPosition? Check for this in the client?)
        try {
            game.makeMove(move);
        }
        catch (InvalidMoveException ex) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ex.getMessage())));
            return;
        }
        // Hopefully gameData is also updated here when we make the move
        gameService.updateGameData(gameData);
        String rootToken = command.getAuthToken();
        NotificationMessage notifyMessage = new NotificationMessage(String.format("%s has made a move",
                command.getUsername()));
        connectionManager.broadcast(gameID, rootToken, notifyMessage);
        LoadGameMessage loadMessage = new LoadGameMessage(gameData);
        connectionManager.broadcast(gameID, rootToken, loadMessage);
    }

    public void leave(LeaveCommand command, Session session) throws DataAccessException, IOException {
        int gameID = command.getGameID();
        String username = command.getUsername();
        String rootToken = command.getAuthToken();
        GameConnection gameConnection = connectionManager.get(gameID);
        if (gameConnection == null) {
            throw new DataAccessException("Unable to access game connection: " + gameID);
        }
        else {
            if (session.isOpen()) {
                session.getRemote().sendString(new Gson().toJson(new NotificationMessage("Goodbye!")));
            }
            switch (command.getUserType()) {
                case OBSERVER -> gameConnection.removeObserver(command.getAuthToken());
                case WHITE_PLAYER -> gameConnection.removeWhitePlayer();
                case BLACK_PLAYER -> gameConnection.removeBlackPlayer();
            }
        }
        String notifyMessage = String.format("%s has left the game", username);
        connectionManager.broadcast(gameID, rootToken, new NotificationMessage(notifyMessage));
    }

    public void resign(ResignCommand command, Session session) throws IOException, DataAccessException {
        if (command.getUserType() == UserGameCommand.UserType.OBSERVER) {
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage("Error: action cannot be performed as observer")));
        }
        else {
            int gameID = command.getGameID();
            String username = command.getUsername();
            String rootToken = command.getAuthToken();
            GameData gameData = gameService.getGameData(gameID);
            gameData.game().setGameOver();
            gameService.updateGameData(gameData);
            session.getRemote().sendString(new Gson().toJson(new NotificationMessage("You have forfeited")));
            String notifyMessage = String.format("%s has forfeited the game", username);
            connectionManager.broadcast(gameID, rootToken, new NotificationMessage(notifyMessage));
        }
    }
}
