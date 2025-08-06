package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private final UserType userType;

    public ConnectCommand(String username, String authToken, Integer gameID, UserType userType) {
        super(CommandType.CONNECT, username, authToken, gameID);
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }
}
