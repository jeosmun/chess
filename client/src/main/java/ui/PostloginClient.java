package ui;

import server.ServerFacade;

public class PostloginClient {
    private final ServerFacade server;
    private final Repl repl;

    public PostloginClient(ServerFacade server, Repl repl) {
        this.server = server;
        this.repl = repl;
    }
}
