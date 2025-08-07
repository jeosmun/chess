package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final GameService gameService;
    private final UserService userService;

    public WebSocketHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        // Maybe we need to catch exceptions here and print them out to the server terminal?
        System.out.println("Raw JSON: " + message);
        System.out.println("Deserialized into: " + command.getClass().getName());
        try {
            switch (command.getCommandType()) {
                case CONNECT -> connect(getConnectCommand(message), session);
                case MAKE_MOVE -> makeMove(getMoveCommand(message), session);
                case LEAVE -> leave(getLeaveCommand(message), session);
                case RESIGN -> resign(getResignCommand(message), session);
            }
        }
        catch (Exception ex) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ex.getMessage())));
        }
    }

    public void connect(ConnectCommand command, Session session) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        ConnectCommand.UserType userType = command.getUserType();
        String rootToken = command.getAuthToken();
        if (!authenticate(rootToken)) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to authenticate")));
            return;
        }
        if (gameService.getGameData(gameID) == null) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: invalid gameID")));
            return;
        }
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
        String rootToken = command.getAuthToken();
        if (!authenticate(rootToken)) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to authenticate")));
            return;
        }
        // Check if observer is trying to make a move
        if (command.getUserType() == MoveCommand.UserType.OBSERVER) {
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage("Error: action cannot be performed as observer")));
            return;
        }
        // Get relevant game data
        int gameID = command.getGameID();
        GameData gameData = gameService.getGameData(gameID);
        ChessGame game = gameData.game();
        if (game.isGameOver()) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: game is over")));
            return;
        }
        ChessMove move = command.getMove();
        MoveCommand.UserType userType = command.getUserType();
        if (userType == MoveCommand.UserType.WHITE_PLAYER && game.getTeamTurn() != ChessGame.TeamColor.WHITE) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: not your turn!")));
            return;
        }
        if (userType == MoveCommand.UserType.BLACK_PLAYER && game.getTeamTurn() != ChessGame.TeamColor.BLACK) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: not your turn!")));
            return;
        }
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
        NotificationMessage notifyMessage = new NotificationMessage(String.format("%s has made a move",
                command.getUsername()));
        connectionManager.broadcast(gameID, rootToken, notifyMessage);
        LoadGameMessage loadMessage = new LoadGameMessage(gameData);
        session.getRemote().sendString(new Gson().toJson(loadMessage));
        connectionManager.broadcast(gameID, rootToken, loadMessage);
    }

    public void leave(LeaveCommand command, Session session) throws DataAccessException, IOException {
        int gameID = command.getGameID();
        String username = command.getUsername();
        String rootToken = command.getAuthToken();
        if (!authenticate(rootToken)) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to authenticate")));
            return;
        }
        LeaveCommand.UserType userType = command.getUserType();
        GameConnection gameConnection = connectionManager.get(gameID);
        if (gameConnection == null) {
            throw new DataAccessException("Unable to access game connection: " + gameID);
        }
        else {
            switch (command.getUserType()) {
                case OBSERVER -> gameConnection.removeObserver(command.getAuthToken());
                case WHITE_PLAYER -> gameConnection.removeWhitePlayer();
                case BLACK_PLAYER -> gameConnection.removeBlackPlayer();
            }
        }
        GameData gameData = gameService.getGameData(gameID);
        if (userType == LeaveCommand.UserType.WHITE_PLAYER) {
            GameData newGameData = new GameData(gameID, null,
                    gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameService.updateGameData(newGameData);
        }
        if (userType == LeaveCommand.UserType.BLACK_PLAYER) {
            GameData newGameData = new GameData(gameID, gameData.whiteUsername(),
                    null, gameData.gameName(), gameData.game());
            gameService.updateGameData(newGameData);
        }
        String notifyMessage = String.format("%s has left the game", username);
        connectionManager.broadcast(gameID, rootToken, new NotificationMessage(notifyMessage));
    }

    public void resign(ResignCommand command, Session session) throws IOException, DataAccessException {
        String rootToken = command.getAuthToken();
        int gameID = command.getGameID();
        GameData gameData = gameService.getGameData(gameID);
        if (gameData.game().isGameOver()) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: game is over")));
            return;
        }
        if (!authenticate(rootToken)) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to authenticate")));
            return;
        }
        if (command.getUserType() == ResignCommand.UserType.OBSERVER) {
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage("Error: action cannot be performed as observer")));
        }
        else {
            String username = command.getUsername();
            gameData.game().setGameOver();
            gameService.updateGameData(gameData);
            session.getRemote().sendString(new Gson().toJson(new NotificationMessage("You have forfeited")));
            String notifyMessage = String.format("%s has forfeited the game", username);
            connectionManager.broadcast(gameID, rootToken, new NotificationMessage(notifyMessage));
        }
    }

    private boolean authenticate(String authToken) {
        try {
            return userService.getAuth(authToken) != null;
        }
        catch (DataAccessException ex) {
            return false;
        }
    }

    private ConnectCommand getConnectCommand(String message) throws DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        AuthData authData = userService.getAuth(command.getAuthToken());
        String username = authData.username();
        int gameID = command.getGameID();
        GameData gameData = gameService.getGameData(gameID);
        ConnectCommand.UserType userType;
        if (Objects.equals(username, gameData.blackUsername())) {
            userType = ConnectCommand.UserType.BLACK_PLAYER;
        }
        else if (Objects.equals(username, gameData.whiteUsername())) {
            userType = ConnectCommand.UserType.WHITE_PLAYER;
        }
        else {
            userType = ConnectCommand.UserType.OBSERVER;
        }
        return new ConnectCommand(userType, username, authData.authToken(), gameID);
    }

    private MoveCommand getMoveCommand(String message) throws DataAccessException {
        MoveCommand command = new Gson().fromJson(message, MoveCommand.class);
        AuthData authData = userService.getAuth(command.getAuthToken());
        String username = authData.username();
        int gameID = command.getGameID();
        GameData gameData = gameService.getGameData(gameID);
        MoveCommand.UserType userType;
        if (Objects.equals(username, gameData.blackUsername())) {
            userType = MoveCommand.UserType.BLACK_PLAYER;
        }
        else if (Objects.equals(username, gameData.whiteUsername())) {
            userType = MoveCommand.UserType.WHITE_PLAYER;
        }
        else {
            userType = MoveCommand.UserType.OBSERVER;
        }
        return new MoveCommand(userType, username, authData.authToken(), gameID, command.getMove());
    }

    private LeaveCommand getLeaveCommand(String message) throws DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, MoveCommand.class);
        AuthData authData = userService.getAuth(command.getAuthToken());
        String username = authData.username();
        int gameID = command.getGameID();
        GameData gameData = gameService.getGameData(gameID);
        LeaveCommand.UserType userType;
        if (Objects.equals(username, gameData.blackUsername())) {
            userType = LeaveCommand.UserType.BLACK_PLAYER;
        }
        else if (Objects.equals(username, gameData.whiteUsername())) {
            userType = LeaveCommand.UserType.WHITE_PLAYER;
        }
        else {
            userType = LeaveCommand.UserType.OBSERVER;
        }
        return new LeaveCommand(userType, username, authData.authToken(), gameID);
    }

    private ResignCommand getResignCommand(String message) throws DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, MoveCommand.class);
        AuthData authData = userService.getAuth(command.getAuthToken());
        String username = authData.username();
        int gameID = command.getGameID();
        GameData gameData = gameService.getGameData(gameID);
        ResignCommand.UserType userType;
        if (Objects.equals(username, gameData.blackUsername())) {
            userType = ResignCommand.UserType.BLACK_PLAYER;
        }
        else if (Objects.equals(username, gameData.whiteUsername())) {
            userType = ResignCommand.UserType.WHITE_PLAYER;
        }
        else {
            userType = ResignCommand.UserType.OBSERVER;
        }
        return new ResignCommand(userType, username, authData.authToken(), gameID);
    }
}
