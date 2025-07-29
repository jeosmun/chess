package ui;

import server.ServerFacade;

public class GameClient {
    public final ServerFacade server;

    public GameClient(ServerFacade server) {
        this.server = server;
    }
}
