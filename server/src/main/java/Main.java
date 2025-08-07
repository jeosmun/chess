import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        var server = new server.Server();

        server.run(8080);
    }
}