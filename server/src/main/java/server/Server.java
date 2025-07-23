package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.RequestConflictException;
import service.*;
import service.requests.*;
import service.results.CreateGameResult;
import service.results.ListGamesResult;
import service.results.LoginResult;
import service.results.RegisterResult;
import spark.*;

import java.util.Map;

public class Server {
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    public Server() {
        try {
            userService = new UserService();
            gameService = new GameService();
            clearService = new ClearService(userService, gameService);
        }
        catch (DataAccessException ex) {
            System.out.println("Error: Unable to access chess database");
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);

        Spark.post("/user", this::register);

        Spark.post("/session", this::login);

        Spark.delete("/session", this::logout);

        Spark.get("/game", this::listGames);

        Spark.post("/game", this::createGame);

        Spark.put("/game", this::joinGame);

        Spark.exception(DataAccessException.class, this::handleDataAccessException);

        Spark.exception(RequestException.class, this::handleRequestException);

        Spark.exception(RequestConflictException.class, this::handleRequestConflictException);

        Spark.exception(AuthException.class, this::handleAuthException);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private record ErrorMessage(String message) {}

    private void handleDataAccessException(DataAccessException ex,Request req, Response res) {
        res.body(new Gson().toJson(new ErrorMessage(ex.getMessage())));
        res.status(500);
    }

    private void handleRequestException(RequestException ex, Request req, Response res) {
        res.body(new Gson().toJson(new ErrorMessage(ex.getMessage())));
        res.status(400);
    }

    private void handleRequestConflictException(RequestConflictException ex, Request req, Response res) {
        res.body(new Gson().toJson(new ErrorMessage(ex.getMessage())));
        res.status(403);
    }

    private void handleAuthException(AuthException ex, Request req, Response res) {
        res.body(new Gson().toJson(new ErrorMessage(ex.getMessage())));
        res.status(401);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) {
        clearService.clear();

        res.status(200);
        res.body("{}");

        return "{}";
    }

    private Object register(Request req, Response res) throws DataAccessException {
        res.type("application/json");
        var body = getBody(req, Map.class);

        if (body.get("username") == null || body.get("password") == null || body.get("email") == null) {
            throw new RequestException("Error: bad request");
        }

        var username = (String) body.get("username");
        var password = (String) body.get("password");
        var email = (String) body.get("email");

        var registerRequest = new RegisterRequest(username, password, email);

        RegisterResult registerResult = userService.register(registerRequest);

        res.status(200);
        res.body(new Gson().toJson(registerResult));

        return new Gson().toJson(registerResult);
    }

    private Object login(Request req, Response res) throws DataAccessException {
        res.type("application/json");
        var body = getBody(req, Map.class);

        if (body.get("username") == null || body.get("password") == null) {
            throw new RequestException("Error: Bad Request");
        }

        var username = (String) body.get("username");
        var password = (String) body.get("password");

        var loginRequest = new LoginRequest(username, password);

        LoginResult loginResult = userService.login(loginRequest);

        res.status(200);
        res.body(new Gson().toJson(loginResult));

        return new Gson().toJson(loginResult);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");

        System.out.printf("AuthToken from Server: %s%n", authToken);

        authenticate(authToken);

        userService.logout(new LogoutRequest(authToken));

        res.status(200);
        res.body("{}");

        return "{}";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        authenticate(authToken);
        ListGamesResult listGamesResult = gameService.listGames(new ListGamesRequest(authToken));

        res.status(200);
        res.body(new Gson().toJson(listGamesResult));
        return new Gson().toJson(listGamesResult);
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        authenticate(authToken);
        res.type("application/json");
        var body = getBody(req, Map.class);

        if (body.get("gameName") == null) {
            throw new RequestException("Error: bad request");
        }

        var gameName = (String) body.get("gameName");

        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(gameName));
        res.status(200);
        res.body(new Gson().toJson(createGameResult));
        return new Gson().toJson(createGameResult);
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        authenticate(authToken);
        res.type("application/json");
        var body = getBody(req, Map.class);

        if (body.get("playerColor") == null || body.get("gameID") == null) {
            throw new RequestException("Error: bad request");
        }

        JoinGameRequest joinGameRequest = new JoinGameRequest((String) body.get("playerColor"),
                (double) body.get("gameID"), userService.getAuth(authToken).username());

        gameService.joinGame(joinGameRequest);

        res.status(200);
        res.body("{}");

        return "{}";
    }

    private void authenticate(String authToken) throws DataAccessException {
        System.out.println("In authenticate");
        if (userService.getAuth(authToken) == null) {
            throw new AuthException("Error: unauthorized");
        }
    }

    private static <T> T getBody(Request req, Class<T> modelClass) {
        var body = new Gson().fromJson(req.body(), modelClass);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
