package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    public enum UserType {
        WHITE_PLAYER,
        BLACK_PLAYER,
        OBSERVER
    }

    private final UserType userType;
    private final String username;

    public ConnectCommand(String authToken, Integer gameID, UserType userType, String username) {
        super(CommandType.CONNECT, authToken, gameID);
        this.userType = userType;
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }
}
