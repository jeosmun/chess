package websocket.commands;

import chess.ChessMove;

public class MoveCommand extends UserGameCommand {
    private final ChessMove move;

    public MoveCommand(UserType userType, String username, String authToken,
                       Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, userType, username, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
