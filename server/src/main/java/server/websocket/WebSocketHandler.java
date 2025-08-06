package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.*;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final GameService gameService;

    public WebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect((ConnectCommand) command);
            case MAKE_MOVE -> makeMove((MoveCommand) command);
            case LEAVE -> leave((LeaveCommand) command);
            case RESIGN -> resign((ResignCommand) command);
        }
    }

    public void connect(ConnectCommand command) {

    }

    public void makeMove(MoveCommand command) {

    }

    public void leave(LeaveCommand command) {

    }

    public void resign(ResignCommand command) {

    }
}
