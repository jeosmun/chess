package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths (
                username VARCHAR(255) NOT NULL,
                authtoken VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
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
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {

    }
}
