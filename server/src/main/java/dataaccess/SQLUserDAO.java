package dataaccess;

import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            );
            """
        };
        DatabaseManager.configureDataBase(createStatements);}

    @Override
    public void clear() throws DataAccessException {
        String[] clearStatement = {"TRUNCATE TABLE users;"};
        DatabaseManager.configureDataBase(clearStatement);
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        var user = getUser(username);
        if (user != null) {
            throw new RequestConflictException("Error: already taken");
        }
        String insertUser = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertStatement = conn.prepareStatement(insertUser)) {
                insertStatement.setString(1, username);
                insertStatement.setString(2, password);
                insertStatement.setString(3, email);
                insertStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String query = "SELECT username, password, email FROM users WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    String resultUsername = rs.getString(1);
                    String resultPassword = rs.getString(2);
                    String resultEmail = rs.getString(3);
                    return new UserData(resultUsername, resultPassword, resultEmail);
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
