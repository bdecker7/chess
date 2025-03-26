package ui;

import serverFacade.ServerFacade;
import chess.ChessGame;
import records.*;

import javax.naming.AuthenticationException;
import java.util.*;

public class ClientPostLogin {

    Scanner scanner = new Scanner(System.in);
    ServerFacade serverFacade = new ServerFacade(8080);
    String authToken = "";
    public HashMap<Integer,Integer> gameIdList = new HashMap<>();


    public ClientPostLogin(String serverUrl, Repl repl) {
    }

    public String evalPost(String input, String preLoginResult) {

        authToken = preLoginResult;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1" -> createGame(params);
                case "2" -> listGame(params);
                case "3" -> joinGame(params);
                case "4" -> observeGame(params);
                case "5" -> logout(params);
                case "6" -> help();

                default -> help();
            };
        } catch (Exception ex){
            return ex.getMessage();
        }
    }

    private String createGame(String[] params) throws Exception {

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

    private String listGame(String[] params) {

        ListGameRequest request = new ListGameRequest(authToken);
        String listOfGamesString = "";
        try{
            ListGameResult result = serverFacade.listGames(request);
            for(int i = 0; i < result.games().size(); i++){
                gameIdList.put(i+1,result.games().get(i).gameID());

                listOfGamesString = listOfGamesString + (i + 1) + ". "
                        + "Game Name: " + result.games().get(i).gameName() + "\n   "
                        + "White Player: " + result.games().get(i).whiteUsername() + "\n   "
                        + "Black Player: " + result.games().get(i).blackUsername() + "\n\n";
            }
            return listOfGamesString;
        }catch (Exception ex){
            return "Unable to List game";
        }

    }

    private String joinGame(String[] params) {

        if(gameIdList.isEmpty()){
            return "Check list of Games first";
        }

        System.out.println("Game Number: ");
        String gameNumber = scanner.nextLine();
        try {
            int gameNum = Integer.parseInt(gameNumber);
            if (!gameIdList.containsKey(gameNum)) {
                return "Invalid Game number";
            }
        } catch (NumberFormatException e) {
            return "Invalid input: Game number must be an integer";
        }

        System.out.println("WHITE/BLACK: ");
        String color = scanner.nextLine();
        ChessGame.TeamColor teamColor = null;

        if (Objects.equals(color, "WHITE")) {
            teamColor = ChessGame.TeamColor.WHITE;
        }else if(Objects.equals(color, "BLACK")){
            teamColor = ChessGame.TeamColor.BLACK;
        }else{
            return "Invalid Color Input";
        }

        if(gameIdList != null) {
            JoinGameRequest joinRequest = new JoinGameRequest(teamColor, gameIdList.get(Integer.parseInt(gameNumber)));
            try {
                if (serverFacade.joinGame(joinRequest, authToken) == 200) {
                    return color;
                } else {
                    return "Unable to Join game";
                }
            } catch (Exception ex) {
                return "Unable to Join game";
            }
        }else{
            return "No games to Join";
        }
    }
    private String observeGame(String[] params){

        int gameNumberInteger;
        if(gameIdList.isEmpty()){
            return "Check list of Games first";
        }

        System.out.println("Game Number: ");
        String gameNumber = scanner.nextLine();
        try {
            int gameNum = Integer.parseInt(gameNumber);
            if (!gameIdList.containsKey(gameNum)) {
                return "Invalid Game number";
            }else{
                gameNumberInteger = gameIdList.get(gameNum);
                return "observer";
            }
        } catch (NumberFormatException e) {
            return "Invalid input: Game number must be an integer";
        }

        //validate that it is a number
        // print the board


    }
    private String logout(String[] params) throws Exception {

        LogOutRequest request = new LogOutRequest(authToken);
        try{
            if (serverFacade.logout(request) == 200){
                return "logged out";
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
                "4. Observe Game\n" +
                "5. Logout\n" +
                "6. Help\n\n" +
                "Request: ";
    }

}
