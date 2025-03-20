package ui;

import java.util.Scanner;

public class Repl {

    private final ClientPreLogin client;

    public Repl(String serverUrl) {
        client = new ClientPreLogin(serverUrl, this);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Bryson's Chess Server!! Type in a command");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
    }
}
