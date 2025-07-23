package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

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
        if (gameName.isEmpty()) {
            throw new DataAccessException("Game Name is empty");
        }
        Random random = new Random();
        int gameID = random.nextInt(0, 9999);
        // Make sure we don't have a repeat gameID
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, newGame);
        String insertGame = "INSERT INTO games (gameID, gameData) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertStatement = conn.prepareStatement(insertGame)) {
                insertStatement.setInt(1, gameID);
                insertStatement.setString(2, new Gson().toJson(gameData));
                insertStatement.executeUpdate();
                return gameData;
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String query = "SELECT gameID, gameData FROM games WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    rs.getInt(1);
                    String resultGameJson = rs.getString(2);
                    return new Gson().fromJson(resultGameJson, GameData.class);
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String update = "UPDATE games SET gameData=? WHERE gameID=?;";
            try (var preparedStatement = conn.prepareStatement(update)) {
                preparedStatement.setString(1, new Gson().toJson(gameData));
                preparedStatement.setInt(2, gameData.gameID());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0 || affectedRows > 1) {
                    throw new DataAccessException(String.format("%d rows affected in game DB", affectedRows));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gameData FROM games;")) {
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    String gameJson = rs.getString(1);
                    gameList.add(new Gson().fromJson(gameJson, GameData.class));
                }
                return gameList;
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
