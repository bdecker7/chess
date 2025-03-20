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
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
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
        return "here_help";
    }

}
