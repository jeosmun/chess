package websocket.commands;

public class MoveCommand extends UserGameCommand {
    public MoveCommand(String authToken, Integer gameID) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
    }
}
