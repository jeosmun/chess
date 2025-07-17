package dataaccess;

import model.GameData;
import java.util.HashMap;
import java.lang.Integer;
import chess.ChessGame;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> gameDB= new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        gameDB.clear();
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        Random random = new Random();
        int gameID = random.nextInt(0, 9999);
        // Make sure we don't have a repeat gameID
        while (gameDB.containsKey(gameID)) {
            gameID = random.nextInt(0, 9999);
        }
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, newGame);
        gameDB.put(gameID, gameData);
        return gameData;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gameDB.get(gameID);
    }
}
