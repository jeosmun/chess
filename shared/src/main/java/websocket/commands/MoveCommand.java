package websocket.commands;

import chess.ChessMove;

public class MoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final UserType userType;

    public MoveCommand(String username, String authToken,
                       Integer gameID, ChessMove move, UserType userType) {
        super(CommandType.MAKE_MOVE, username, authToken, gameID);
        this.move = move;
        this.userType = userType;
    }

    public ChessMove getMove() {
        return move;
    }

    public UserType getUserType() {
        return userType;
    }
}
