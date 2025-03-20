package ui;

import java.util.Arrays;

public class ClientPostLogin {

    public ClientPostLogin(String serverUrl, Repl repl) {
    }

    public String evalPost(String input ) {

        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1" -> create_game(params);
                case "2" -> list_game(params);
                case "3" -> join_game(params);
                case "4" -> play_game(params);
                case "5" -> observe_game(params);
                case "6" -> logout(params);

                default -> help();
            };
        } catch (InvalidRequest ex) {
            return ex.getMessage();
        }
    }

    private String create_game(String[] params) {
        //call serverfacade class
        return "here_createGame";
    }

    private String list_game(String[] params) {
        return "here_listGame";
    }

    private String join_game(String[] params){
        return "here_joinGame";
    }
    private String play_game(String[] params){
        return "here_playGame";
    }
    private String observe_game(String[] params){
        return "here_observeGame";
    }
    private String logout(String[] params){
        return "here_logout";
    }

    String help() {

        return "Type the number you wish that coorelates with what you would like to do: \n \n" +
                "1. Create Game\n" +
                "2. List Games\n" +
                "3. Join Game\n" +
                "4. Play Game\n" +
                "5. Observe Game\n" +
                "6. Logout\n\n" +
                "Request: ";
    }

}
