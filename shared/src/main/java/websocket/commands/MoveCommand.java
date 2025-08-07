package websocket.commands;

import chess.ChessMove;

public class MoveCommand extends UserGameCommand {

    private final ChessMove move;
    private final UserType userType;
    private final String username;

    public MoveCommand(UserType userType, String username, String authToken,
                       Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
        this.userType = userType;
        this.username = username;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }
}
