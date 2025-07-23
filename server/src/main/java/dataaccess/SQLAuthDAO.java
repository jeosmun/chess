package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(255) NOT NULL,
                authtoken VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            );
            """
        };
        DatabaseManager.configureDataBase(createStatements);}

    @Override
    public void clear() throws DataAccessException {
        String[] clearStatement = {"TRUNCATE TABLE auths"};
        DatabaseManager.configureDataBase(clearStatement);
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        String insertAuth = "INSERT INTO auths (username, authtoken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertStatement = conn.prepareStatement(insertAuth)) {
                insertStatement.setString(1, username);
                insertStatement.setString(2, authToken);
                insertStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String selectAuth = "SELECT username, authtoken FROM auths WHERE authtoken=?";
        try {
            return queryAuth(selectAuth, authToken);
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        System.out.println(authData.authToken());
        String deleteAuth = "DELETE FROM auths WHERE authtoken=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var deleteStatement = conn.prepareStatement(deleteAuth)) {
                deleteStatement.setString(1, authData.authToken());
                int affectedRows = deleteStatement.executeUpdate();
                if (affectedRows == 0 || affectedRows > 1) {
                    throw new DataAccessException(String.format("%d rows deleted from auth DB", affectedRows));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private AuthData queryAuth(String query, String value) throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var selectStatement = conn.prepareStatement(query)) {
                selectStatement.setString(1, value);
                try (var rs = selectStatement.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    String resultUsername = rs.getString(1);
                    String resultAuthToken = rs.getString(2);
                    return new AuthData(resultAuthToken, resultUsername);
                }
            }
        }
    }
}
