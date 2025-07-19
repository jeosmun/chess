package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.RegisterResult;
import spark.*;

import javax.xml.crypto.Data;
import java.util.Map;

public class Server {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();
    private final ClearService clearService = new ClearService();

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

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) {
        return null;
    }

    private Object register(Request req, Response res) {
        res.type("application/json");
        var body = getBody(req, Map.class);

        var username = (String) body.get("username");
        var password = (String) body.get("password");
        var email = (String) body.get("email");

        var registerRequest = new RegisterRequest(username, password, email);
        System.out.println(registerRequest);

        RegisterResult registerResult = userService.register(registerRequest);
        System.out.println(registerResult.toString());

        res.status(200);
        res.body(new Gson().toJson(registerResult));

        return new Gson().toJson(registerResult);
    }

    private Object login(Request req, Response res) {
        res.type("application/json");
        var body = getBody(req, Map.class);

        var username = (String) body.get("username");
        var password = (String) body.get("password");

        var loginRequest = new LoginRequest(username, password);
        System.out.println(loginRequest.toString());

        LoginResult loginResult = userService.login(loginRequest);
        System.out.println(loginResult.toString());

        res.status(200);
        res.body(new Gson().toJson(loginResult));

        return new Gson().toJson(loginResult);
    }

    private Object logout(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            if (userService.getAuth(authToken) == null) {
                // Return a message about being unauthorized
            }
            userService.logout(new LogoutRequest(authToken));

            res.status(200);
            res.body("{}");

            return "{}";
        }
        catch (DataAccessException ex) {
            // Do something with the DataAccessException
        }
        return null;
    }

    private Object listGames(Request req, Response res) {
        return null;
    }

    private Object createGame(Request req, Response res) {
        return null;
    }

    private Object joinGame(Request req, Response res) {
        return null;
    }

    private boolean authenticate(String authToken) {
        try {
            if (userService.getAuth(authToken) == null) {
                // Return a message about being unauthorized
            }
        }
        catch (DataAccessException ex) {
            // Do something with the DataAccessException
        }
        return false;
    }

    private static <T> T getBody(Request req, Class<T> modelClass) {
        var body = new Gson().fromJson(req.body(), modelClass);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
