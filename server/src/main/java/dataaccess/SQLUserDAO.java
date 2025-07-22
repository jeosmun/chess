package dataaccess;

import model.UserData;

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
        String[] clearStatement = {"TRUNCATE TABLE users"};
        DatabaseManager.configureDataBase(clearStatement);
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
