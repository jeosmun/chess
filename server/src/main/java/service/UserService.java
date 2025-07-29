package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

public class UserService {
    private final UserDAO userDAO = new SQLUserDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();

    public UserService() throws DataAccessException {
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, RequestConflictException {
        AuthData authData = authDAO.createAuth(registerRequest.username());
        userDAO.createUser(registerRequest.username(), hashPassword(registerRequest.password()), registerRequest.email());
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        try {
            UserData userData = userDAO.getUser(loginRequest.username());
            if (userData == null || !BCrypt.checkpw(loginRequest.password(), userData.password())) {
                throw new AuthException("unauthorized");
            }
            else {
                AuthData authData = authDAO.createAuth(loginRequest.username());
                return new LoginResult(authData.username(), authData.authToken());
            }
        }
        catch (DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        System.out.println(logoutRequest);

        AuthData authData = authDAO.getAuth(logoutRequest.authToken());
        if (authData == null) {
            throw new AuthException("unauthorized");
        }
        System.out.println(authData.authToken());
        authDAO.deleteAuth(authData);

    }

    public void clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken);
    }

    String hashPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }
}
