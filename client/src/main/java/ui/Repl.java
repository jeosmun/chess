package ui;

import server.ServerFacade;

public class Repl {
    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private final GameClient gameClient;
    private final ServerFacade server;

    public Repl(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        preloginClient = new PreloginClient(server);
        postloginClient = new PostloginClient(server);
        gameClient = new GameClient(server);
    }

    public void run() {

    }
}
