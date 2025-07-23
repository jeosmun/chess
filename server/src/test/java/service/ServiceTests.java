package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.RequestConflictException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.requests.*;
import service.results.CreateGameResult;
import service.results.ListGamesResult;
import service.results.LoginResult;
import service.results.RegisterResult;

import java.util.ArrayList;

public class ServiceTests {

    private static GameService gameService;
    private static UserService userService;
    private static ClearService clearService;
    private static UserData user1;
    private static UserData user2;

    @BeforeAll
    public static void init() throws DataAccessException {
        user1 = new UserData("username1","password1","email1");
        user2 = new UserData("username2", "password2", "email2");
        gameService = new GameService();
        userService = new UserService();
        clearService = new ClearService(userService, gameService);
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        clearService.clear();

        RegisterResult registerResult = userService.register(new RegisterRequest(user1.username(), user1.password(), user1.email()));

        userService.logout(new LogoutRequest(registerResult.authToken()));
    }

    @Test
    @DisplayName("Clear")
    public void clearSuccess() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));

        RegisterResult registerResult = userService.register(new RegisterRequest(user2.username(), user2.password(),
                user2.email()));

        gameService.createGame(new CreateGameRequest("testGame"));

        clearService.clear();

        ArrayList<GameData> gameList = gameService.listGames(new ListGamesRequest(loginResult.authToken())).games();

        Assertions.assertTrue(gameList.isEmpty());
        Assertions.assertNull(userService.getAuth(loginResult.authToken()));
        Assertions.assertNull(userService.getAuth(registerResult.authToken()));
        Assertions.assertThrows(AuthException.class, () -> userService.login(new LoginRequest(user1.username(),
                user1.password())));
        Assertions.assertThrows(AuthException.class, () -> userService.login(new LoginRequest(user2.username(),
                user2.password())));

    }

    @Test
    @DisplayName("Normal User Login")
    public void loginSuccess() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));

        Assertions.assertEquals(user1.username(), loginResult.username());
        Assertions.assertNotNull(loginResult.authToken());
    }

    @Test
    @DisplayName("Wrong password")
    public void wrongPassword() {
        Assertions.assertThrows(AuthException.class, () -> userService.login(new LoginRequest(user1.username(), "wrongPassword")));
    }

    @Test
    @DisplayName("Normal User Logout")
    public void logoutSuccess() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));
        Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(loginResult.authToken())));
    }

    @Test
    @DisplayName("Unauthorized Logout")
    public void logoutFails() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));
        Assertions.assertThrows(AuthException.class, () -> userService.logout(new LogoutRequest(null)));
    }

    @Test
    @DisplayName("Normal User Registration")
    public void registrationSuccess() throws DataAccessException {
        RegisterResult registerResult = userService.register(new RegisterRequest(user2.username(), user2.password(),
                user2.email()));
        Assertions.assertNotNull(registerResult.authToken());
    }

    @Test
    @DisplayName("Username already taken")
    public void usernameTaken() {
        Assertions.assertThrows(RequestConflictException.class, () -> userService.register(
                new RegisterRequest(user1.username(), user2.password(), user2.email())));
    }

    @Test
    @DisplayName("Game List Empty")
    public void gameListEmpty() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));

        ListGamesResult listGamesResult = gameService.listGames(new ListGamesRequest(loginResult.authToken()));

        Assertions.assertTrue(listGamesResult.games().isEmpty());
    }

    @Test
    @DisplayName("Normal Create Game")
    public void createGameSuccess() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));

        gameService.createGame(new CreateGameRequest("testGame"));

        Assertions.assertNotNull(gameService.listGames(new ListGamesRequest(loginResult.authToken())));
    }

    @Test
    @DisplayName("Unauthorized Create Game")
    public void createGameFails() {
        Assertions.assertThrows(RequestException.class, () -> gameService.createGame(new CreateGameRequest(null)));
    }

    @Test
    @DisplayName("Game List Include Created Game")
    public void gameListHasGame() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));

        gameService.createGame(new CreateGameRequest("testGame"));

        ListGamesResult listGamesResult = gameService.listGames(new ListGamesRequest(loginResult.authToken()));

        Assertions.assertInstanceOf(GameData.class, listGamesResult.games().getFirst());
    }

    @Test
    @DisplayName("Game Does Not Exist")
    public void gameDoesNotExist() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));

        Assertions.assertThrows(RequestException.class, () -> gameService.joinGame(
                new JoinGameRequest("WHITE", 9999, loginResult.username())));
    }

    @Test
    @DisplayName("Normal Join Game")
    public void joinGameSuccess() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));

        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest("testGame"));

        Assertions.assertDoesNotThrow(() ->gameService.joinGame(new JoinGameRequest("WHITE", createGameResult.gameID(),
                loginResult.username())));
    }

    @Test
    @DisplayName("Join Game Color Taken")
    public void joinGameFails() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest(user1.username(), user1.password()));

        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest("testGame"));

        Assertions.assertDoesNotThrow(() -> gameService.joinGame(new JoinGameRequest("WHITE",
                createGameResult.gameID(), loginResult.username())));

        Assertions.assertThrows(RequestConflictException.class, () -> gameService.joinGame(new JoinGameRequest("WHITE",
                createGameResult.gameID(), loginResult.username())));
    }
}
