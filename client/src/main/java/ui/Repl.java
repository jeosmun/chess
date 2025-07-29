package ui;

import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

import static ui.State.*;

public class Repl {
    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private final GameClient gameClient;
    private final ServerFacade server;
    private State state;

    public Repl(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        preloginClient = new PreloginClient(server);
        postloginClient = new PostloginClient(server);
        gameClient = new GameClient(server);
        state = SIGNEDOUT;
    }

    public void run() {
        printItalics("Welcome to the Chess Server!");
        System.out.print("\n");
        printHelp();

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
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
//            case SIGNEDIN -> postloginClient.help();
//            case INGAME -> gameClient.help();
        }
        System.out.println(helpText);
    }

    public String eval(String input) {
        String result = "";
        switch (state) {
            case SIGNEDOUT -> result = preloginClient.eval(input);
        }
        return result;
    }
}
