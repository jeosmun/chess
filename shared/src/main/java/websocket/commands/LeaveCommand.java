package websocket.commands;

public class LeaveCommand extends UserGameCommand {

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
