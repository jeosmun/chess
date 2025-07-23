package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.RegisterResult;

import java.util.Objects;

public class UserService {
    private final UserDAO userDAO = new SQLUserDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();

    public UserService() throws DataAccessException {
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, RequestConflictException {
        AuthData authData = authDAO.createAuth(registerRequest.username());
        userDAO.createUser(registerRequest.username(), registerRequest.password(), registerRequest.email());
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) {
        try {
            UserData userData = userDAO.getUser(loginRequest.username());
            if (userData == null || !Objects.equals(userData.password(), loginRequest.password())) {
                throw new AuthException("Error: unauthorized");
            }
            else {
                AuthData authData = authDAO.createAuth(loginRequest.username());
                return new LoginResult(authData.username(), authData.authToken());
            }
        }
        catch (DataAccessException ex) {

        }
        return null;
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        System.out.println(logoutRequest);

        AuthData authData = authDAO.getAuth(logoutRequest.authToken());
        if (authData == null) {
            throw new AuthException("Error: Unauthorized");
        }
        System.out.println(authData.authToken());
        authDAO.deleteAuth(authData);

    }

    public void clear() {
        try {
            userDAO.clear();
            authDAO.clear();
        }
        catch (Exception ex) {

        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken);
    }
}
