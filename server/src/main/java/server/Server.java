package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        // Why did this from the given code note work?
        // Spark.staticFiles.location("/web");

        Spark.staticFiles.externalLocation("/Users/osmun/Documents/cs240/chess/server/src/main/resources/web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
