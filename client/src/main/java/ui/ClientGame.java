package ui;

import java.util.Arrays;

public class ClientGame {
    public ClientGame(String serverUrl, Repl repl) {
    }

    public String eval(String input ) {

        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
//                case "1" -> new DrawChessBoard();
                case "2" -> "quit";
                default -> help();
            };
        } catch (InvalidRequest ex) {
            return ex.getMessage();
        }
    }

    private String help() {
        return "game_help";
    }
}
