package ui;

import ServerFacade.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
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
                case "1" -> create_game(params);
                case "2" -> list_game(params);
                case "3" -> join_game(params);
                case "4" -> observe_game(params);
                case "5" -> logout(params);

                default -> help();
            };
        } catch (InvalidRequest ex) {
            return ex.getMessage();
        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    private String create_game(String[] params) throws Exception {

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

    private String join_game(String[] params) {

        System.out.println("Game Number: ");
        String gameNumber = scanner.nextLine();
        System.out.println("WHITE/BLACK: ");
        String color = scanner.nextLine();
        ChessGame.TeamColor teamColor = null;

        if (color == "WHITE") {
            teamColor = ChessGame.TeamColor.WHITE;
        }else if(color == "BLACK"){
            teamColor = ChessGame.TeamColor.BLACK;
        }else{
            return "Invalid Color Input";
        }
        JoinGameRequest request = new JoinGameRequest(teamColor, gameIdList.get(Integer.parseInt(gameNumber)));

        try{
            if (serverFacade.joinGame(request) == 200){
                return color;
            }else {
                return "Unable to Join game";
//                throw new Exception("Unable to Join");
            }
        }catch (Exception ex){
            return "Unable to Join game";
        }

    }
    private String observe_game(String[] params){
        System.out.println("Game Number: ");
        String gameNumber = scanner.nextLine();

        ChessGame.TeamColor teamColor = null;
        JoinGameRequest request = new JoinGameRequest(teamColor, gameIdList.get(Integer.parseInt(gameNumber)));

        try{
            if (serverFacade.joinGame(request) == 200){
                return "Successful Observe";
            }else {
                throw new Exception("Unable to Observe");
            }
        }catch (Exception ex){
            return "Unable to Observe";
        }
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
                "5. Logout\n\n" +
                "Request: ";
    }

}
