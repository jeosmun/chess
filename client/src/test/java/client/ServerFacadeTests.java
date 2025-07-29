package client;

import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import server.Server;
import server.ServerFacade;
import results.ListGamesResult;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setUp() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        facade.clear();
        server.stop();
    }

    @Test
    @DisplayName("Clear Test")
    public void clearSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    @DisplayName("Register Test")
    public void registerSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
    }

    @Test
    @DisplayName("Register Test Fails")
    public void registerFail() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
        Assertions.assertThrows(ResponseException.class, () -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
    }

    @Test
    @DisplayName("Logout Test")
    public void logoutSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    @DisplayName("Logout Test Fails")
    public void logoutFail() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
        Assertions.assertDoesNotThrow(() -> facade.logout());
        Assertions.assertThrows(ResponseException.class, () -> facade.logout());
    }

    @Test
    @DisplayName("Login Test")
    public void loginSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
        Assertions.assertDoesNotThrow(() -> facade.logout());
        Assertions.assertDoesNotThrow(() -> facade.login(new LoginRequest("testUser1", "testUser1")));
    }

    @Test
    @DisplayName("Login Test Fails")
    public void loginFails() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
        Assertions.assertDoesNotThrow(() -> facade.logout());
        Assertions.assertThrows(ResponseException.class,
                () -> facade.login(new LoginRequest("testUser1", "testUser2")));
    }

    @Test
    @DisplayName("List Test")
    public void listSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
        Assertions.assertDoesNotThrow(() -> facade.list());
    }

    @Test
    @DisplayName("List Test Fails")
    public void listFails() {
        Assertions.assertThrows(ResponseException.class, () -> facade.list());
    }

    @Test
    @DisplayName("Create Test")
    public void createSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
        Assertions.assertDoesNotThrow(() -> facade.create(new CreateGameRequest("testGame1")));
    }

    @Test
    @DisplayName("Create Test Fails")
    public void createFails() {
        Assertions.assertThrows(ResponseException.class,
                () -> facade.create(new CreateGameRequest("testGame1")));
    }

    @Test
    @DisplayName("Join Test")
    public void joinSuccess() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> facade.register(new RegisterRequest("testUser1",
                "testUser1", "testUser1@email.com")));
        Assertions.assertDoesNotThrow(() -> facade.create(new CreateGameRequest("testGame1")));
        ListGamesResult res = facade.list();
        GameData gameData = res.games().getFirst();
        Assertions.assertDoesNotThrow(
                () -> facade.join(new JoinGameRequest("WHITE", gameData.gameID(), "testUser1")));
    }

    @Test
    @DisplayName("Join Test Fails")
    public void joinFails() {
        Assertions.assertThrows(ResponseException.class,
                () -> facade.join(new JoinGameRequest("WHITE", 1234, "testUser1")));
    }
}
