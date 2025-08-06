package websocket.commands;

public class ResignCommand extends UserGameCommand {
    public ResignCommand(UserType userType, String username, String authToken, Integer gameID) {
        super(CommandType.RESIGN, userType, username, authToken, gameID);
    }
}
