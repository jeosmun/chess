package dataaccess;

import model.GameData;

public interface GameDAO {
    void clear() throws DataAccessException;

    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    java.util.ArrayList<GameData> listGames() throws DataAccessException;
}
