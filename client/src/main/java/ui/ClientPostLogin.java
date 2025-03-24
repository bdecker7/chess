package ui;

import ServerFacade.ServerFacade;
import records.*;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.Scanner;

public class ClientPostLogin {

    Scanner scanner = new Scanner(System.in);
    ServerFacade serverFacade = new ServerFacade();
    String authToken = "";

    public ClientPostLogin(String serverUrl, Repl repl) {
    }

    public String evalPost(String input, String preLoginResult) {

        authToken = preLoginResult;
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
        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    private String create_game(String[] params) throws Exception {
        //call serverfacade class
        System.out.println("Game Name: ");
        String gameName = scanner.nextLine();
        CreateGameRequest request = new CreateGameRequest(authToken,gameName);

        CreateGameResult result = serverFacade.createGame(request);

        if(result != null){
            System.out.println("Game successfully created!");

            return "Successful Game Creation";
        }

        return "Not Successful";
    }

    private String list_game(String[] params) {

        ListGameRequest request = new ListGameRequest(authToken);
        String listOfGamesString = "";
        try{
            ListGameResult result = serverFacade.listGames(request);
            for(int i = 0; i < result.games().size(); i++){
                listOfGamesString = listOfGamesString + Integer.toString(i+1) + ". " + result.games().get(i) + "\n" ;
            }
            return listOfGamesString;
        }catch (Exception ex){
            return ex.getMessage();
        }

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
    private String logout(String[] params) throws Exception {

        System.out.println("Authoriazation Token: ");
        String authString = scanner.nextLine();
        LogOutRequest request = new LogOutRequest(authString);
        try{
            if (serverFacade.logout(request) == 200){
                return "logout";
            }else {
                throw new AuthenticationException("Incorrect AuthToken, not Authorized");
            }
        }catch (Exception ex){
            return ex.getMessage();
        }
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
