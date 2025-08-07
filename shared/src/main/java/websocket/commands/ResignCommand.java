package websocket.commands;

public class ResignCommand extends UserGameCommand {
    public enum UserType {
        WHITE_PLAYER,
        BLACK_PLAYER,
        OBSERVER
    }

    private final UserType userType;
    private final String username;

    public ResignCommand(UserType userType, String username, String authToken, Integer gameID) {
        super(CommandType.RESIGN, authToken, gameID);
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
