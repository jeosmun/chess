package dataaccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
                gameID INT NOT NULL,
                gameData TEXT NOT NULL,
                PRIMARY KEY (gameID)
            );
            """
        };
        DatabaseManager.configureDataBase(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        String[] clearStatement = {"TRUNCATE TABLE games"};
        DatabaseManager.configureDataBase(clearStatement);
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) {

    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }
}
