package ui;

import java.util.Arrays;

public class ClientPreLogin {

    public ClientPreLogin(String serverUrl, Repl repl) {
    }

    public String eval(String input ) {

        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1" -> register(params);
                case "2" -> login(params);
                case "3" -> "quit";
                default -> help();
            };
        } catch (InvalidRequest ex) {
            return ex.getMessage();
        }
    }

    private String login(String[] params) {
        //call serverfacade class
        return "here_login";
    }

    private String register(String[] params) {
        return "here_register";
    }

    String help() {

        return "Type the number you wish that coorelates with what you would like to do: \n \n" +
                "1. Register\n" +
                "2. Login\n" +
                "3. Quit\n" +
                "\n" +
                "Request: ";
    }

}
