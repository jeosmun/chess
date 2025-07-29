package ui;

import server.ServerFacade;

public class GameClient {
    public final ServerFacade server;
    public final Repl repl;

    public GameClient(ServerFacade server, Repl repl) {
        this.server = server;
        this.repl = repl;
    }
}
