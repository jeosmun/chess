package ui;

import server.ServerFacade;
import server.websocket.NotificationHandler;
import websocket.messages.*;

import java.util.Scanner;

import static ui.EscapeSequences.*;

import static ui.State.*;

public class Repl implements NotificationHandler {
    public final PreloginClient preloginClient;
    public final PostloginClient postloginClient;
    public final GameClient gameClient;
    private final ServerFacade server;
    private State state;
    private boolean showHelp = true;

    public Repl(String serverUrl) {
        this.server = new ServerFacade(serverUrl, this);
        preloginClient = new PreloginClient(server, this);
        postloginClient = new PostloginClient(server, this);
        gameClient = new GameClient(server, this);
        state = SIGNEDOUT;
    }

    public void run() {
        printItalics("Welcome to the Chess Server!");
        System.out.print("\n");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!(result.equals("quit") && this.state == SIGNEDOUT)) {
            if (showHelp) {
                printHelp();
                showHelp = false;
            }
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                if (result.equals("quit")) {
                    printItalics("Thank you for using this Chess server!");
                    break;
                }
                    System.out.println(SET_TEXT_COLOR_BLUE + result + RESET_TEXT_COLOR);
            }
            catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void printPrompt() {
        switch (state) {
            case SIGNEDOUT -> System.out.print("Chess Login >>> ");
            case SIGNEDIN -> System.out.print("Chess >>> ");
            case INGAME -> System.out.print("Chess Game >>> ");
        }
    }

    public void printHelp() {
        String helpText = "";
        switch (state) {
            case SIGNEDOUT -> helpText = preloginClient.help();
            case SIGNEDIN -> helpText = postloginClient.help();
            case INGAME -> helpText = gameClient.help();
        }
        System.out.println(helpText);
    }

    public String eval(String input) {
        String result = "";
        switch (state) {
            case SIGNEDOUT -> result = preloginClient.eval(input);
            case SIGNEDIN -> result = postloginClient.eval(input);
            case INGAME -> result = gameClient.eval(input);
        }
        return result;
    }

    public void setState(State state) {
        this.state = state;
        this.showHelp = true;
    }

    // Implementing a websocket functionality. Turning Repl into a NotificationHandler object

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case ERROR -> {
                error((ErrorMessage) message);
            }
            case LOAD_GAME -> {
                loadGame((LoadGameMessage) message);
            }
            case NOTIFICATION -> {
                notify((NotificationMessage) message);
            }
        }
    }

    public void error(ErrorMessage message) {
        System.out.println(SET_TEXT_COLOR_RED + message.getMessage() + RESET_TEXT_COLOR);
        printPrompt();
    }

    public void loadGame(LoadGameMessage message) {
        // This function just prints out the board from both points of view.
        // Need to add functionality for printing out differently for white/black/observers
        gameClient.setGameData(message.getGameData());
        System.out.print(gameClient.loadBoard());
        printPrompt();
    }

    public void notify(NotificationMessage message) {
        System.out.println(SET_TEXT_COLOR_BLUE + message.getMessage() + RESET_TEXT_COLOR);
        printPrompt();
    }
}
