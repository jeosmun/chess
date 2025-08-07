package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    public enum UserType {
        WHITE_PLAYER,
        BLACK_PLAYER,
        OBSERVER
    }

    public LeaveCommand(UserType userType, String username, String authToken, Integer gameID) {
        super(CommandType.LEAVE, authToken, gameID);
        this.username = username;
        this.userType = userType;
    }

    private final String username;
    private final UserType userType;

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }
}
