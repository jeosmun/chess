package websocket.commands;

public class ConnectCommand extends UserGameCommand {

    public ConnectCommand(UserType userType, String username, String authToken, Integer gameID) {
        super(CommandType.CONNECT, userType, username, authToken, gameID);
    }
}
