package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    public LeaveCommand(UserType userType, String username, String authToken, Integer gameID) {
        super(CommandType.LEAVE, userType, username, authToken, gameID);
    }
}
