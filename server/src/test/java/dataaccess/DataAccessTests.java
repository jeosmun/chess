package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

public class DataAccessTests {

    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;
    private static UserData user1;
    private static UserData user2;

    @BeforeAll
    public static void init() throws DataAccessException {
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
        user1 = new UserData("username1","password1","email1");
        user2 = new UserData("username2", "password2", "email2");
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Clear User")
    public void userClearSuccess() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(user1.username(), user1.password(), user1.email()));
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(user2.username(), user2.password(), user2.email()));

        Assertions.assertDoesNotThrow(() -> userDAO.clear());

        Assertions.assertNull(userDAO.getUser(user1.username()));
        Assertions.assertNull(userDAO.getUser(user2.username()));
    }

    @Test
    @DisplayName("Clear Auth")
    public void authClearSuccess() throws DataAccessException {
        final AuthData[] authList = new AuthData[2];
        Assertions.assertDoesNotThrow(() -> {
            authList[0] = authDAO.createAuth(user1.username());});
        Assertions.assertDoesNotThrow(() -> {
            authList[1] = authDAO.createAuth(user2.username());});

        Assertions.assertDoesNotThrow(() -> authDAO.clear());

        Assertions.assertNull(authDAO.getAuth(authList[0].authToken()));
        Assertions.assertNull(authDAO.getAuth(authList[1].authToken()));
    }

    @Test
    @DisplayName("Clear Game")
    public void gameClearSuccess() throws DataAccessException {
        final GameData[] gameList = new GameData[2];
        Assertions.assertDoesNotThrow(() -> {
            gameList[0] = gameDAO.createGame("game1");});
        Assertions.assertDoesNotThrow(() -> {
            gameList[1] = gameDAO.createGame("game2");});

        Assertions.assertDoesNotThrow(() -> gameDAO.clear());

        Assertions.assertNull(gameDAO.getGame(gameList[0].gameID()));
        Assertions.assertNull(gameDAO.getGame(gameList[1].gameID()));
    }

    @Test
    @DisplayName("Normal Create User")
    public void createUserSuccess() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(user1.username(), user1.password(), user1.email()));

        UserData userData = userDAO.getUser(user1.username());

        Assertions.assertEquals(user1.username(), userData.username());
        Assertions.assertEquals(user1.password(), userData.password());
        Assertions.assertEquals(user1.email(), userData.email());
    }

    @Test
    @DisplayName("Register same username twice")
    public void registerTwiceFails() throws DataAccessException {
        userDAO.createUser(user1.username(), user1.password(), user1.email());

        Assertions.assertThrows(RequestConflictException.class,
                () -> userDAO.createUser(user1.username(), user1.password(), user1.email()));
    }

    @Test
    @DisplayName("Get User Success")
    public void getUserSuccess() throws DataAccessException {
        userDAO.createUser(user1.username(), user1.password(), user1.email());

        Assertions.assertNotNull(userDAO.getUser(user1.username()));
    }

    @Test
    @DisplayName("Get User Fails")
    public void getUserFails() throws DataAccessException {
        Assertions.assertNull(userDAO.getUser(user1.username()));
    }

    @Test
    @DisplayName("Normal Create Auth")
    public void createAuthSuccess() throws DataAccessException {
        final AuthData[] authList = new AuthData[1];
        Assertions.assertDoesNotThrow(() -> {
            authList[0] = authDAO.createAuth(user1.username());});

        Assertions.assertNotNull(authDAO.getAuth(authList[0].authToken()));
    }

    @Test
    @DisplayName("Create Auth Fails")
    public void createAuthFails() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }

    @Test
    @DisplayName("Get Auth Success")
    public void getAuthSuccess() throws DataAccessException {
        AuthData inputData = authDAO.createAuth(user1.username());

        AuthData outputData = authDAO.getAuth(inputData.authToken());

        Assertions.assertEquals(inputData.username(), outputData.username());
    }

    @Test
    @DisplayName("Get Auth Fails")
    public void getAuthFails() throws DataAccessException {
        Assertions.assertNull(authDAO.getAuth(""));
    }

    @Test
    @DisplayName("Normal Delete Auth")
    public void deleteAuthSuccess() throws DataAccessException {
        AuthData inputData = authDAO.createAuth(user1.username());

        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(inputData));
    }

    @Test
    @DisplayName("Delete Auth Fails")
    public void deleteAuthFails() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class,
                () -> authDAO.deleteAuth(new AuthData("", "")));
    }

    @Test
    @DisplayName("Normal Create Game")
    public void createGameSuccess() throws DataAccessException {
        final GameData[] gameList = new GameData[1];
        Assertions.assertDoesNotThrow(() -> {
            gameList[0] = gameDAO.createGame("game1");});

        Assertions.assertNotNull(gameDAO.getGame(gameList[0].gameID()));
    }

    @Test
    @DisplayName("Create Game Fails")
    public void createGameFails() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game1"));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(""));
    }

    @Test
    @DisplayName("Normal Get Game")
    public void getGameSuccess() throws DataAccessException {
        final GameData[] gameList = new GameData[1];
        Assertions.assertDoesNotThrow(() -> {
            gameList[0] = gameDAO.createGame("game1");});
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(gameList[0].gameID()));
    }

    @Test
    @DisplayName("Get Game Fails")
    public void getGameFails() throws DataAccessException {
        Assertions.assertNull(gameDAO.getGame(10000));
    }

    @Test
    @DisplayName("Normal Update Game")
    public void updateGameSuccess() throws DataAccessException {
        final GameData[] gameList = new GameData[2];
        Assertions.assertDoesNotThrow(() -> {
            gameList[0] = gameDAO.createGame("game1");});
        Assertions.assertDoesNotThrow(() -> {
            gameList[1] = gameDAO.createGame("game2");});

        GameData newGame = new GameData(gameList[0].gameID(), "josmun", null,
                gameList[0].gameName(), gameList[0].game());

        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(newGame));

        Assertions.assertEquals(newGame, gameDAO.getGame(gameList[0].gameID()));
    }

    @Test
    @DisplayName("Update Game Fails")
    public void updateGameFails() throws DataAccessException {
        GameData game = new GameData(1234, null,
                null, "testGame", new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(game));
    }

    @Test
    @DisplayName("Normal List Games")
    public void listGamesSuccess() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game1"));
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game2"));

        Assertions.assertNotNull(gameDAO.listGames());
    }

    @Test
    @DisplayName("List Games Fails")
    public void listGamesFails() throws DataAccessException {
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
    }
}
