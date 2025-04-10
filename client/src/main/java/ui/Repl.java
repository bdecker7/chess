package ui;

import chess.ChessGame;
import serverfacade.ServerMessageObserver;

import java.util.Objects;
import java.util.Scanner;

public class Repl implements ServerMessageObserver {

    private final ClientPreLogin clientPreLogin;
    private final ClientPostLogin clientPostLogin;
    private final ClientGame clientGame;
    private static Integer status = 0;
    private String preLoginResult = null;
    private PostLoginResult postResult = null;
    private String playerColor = null;
    private String gameResult = null;
    public ChessGame game = new ChessGame();

    public Repl(String serverUrl) {
        clientPreLogin = new ClientPreLogin(serverUrl, this);
        clientPostLogin = new ClientPostLogin(serverUrl,this);
        clientGame = new ClientGame(serverUrl, this);
    }

    public void run() {
        System.out.println("♘♜ Welcome to Bryson's Chess Server!! Type in a command");
        System.out.print(clientPreLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {

            String line = scanner.nextLine();

            try {
                if(status == 0){
                    preLoginResult = clientPreLogin.eval(line);
                    if(preLoginResult.length() == 36){
                        status = 1;
                    }else if(preLoginResult.equals("quit")){
                        result = "quit";
                    }
                    else{
                        status = 0;
                        System.out.print(preLoginResult);
                    }

                }else if(status == 1) {
                    //add here if want to have the menu pop up before
                    postResult = clientPostLogin.evalPost(line, preLoginResult);

                    System.out.println(postResult.message());

                    if(Objects.equals(postResult.message(), "logged out")){
                        status = 0;
                    }else if(Objects.equals(postResult.message(), "WHITE") || Objects.equals(postResult.message(), "BLACK")){
                        status = 2;
                        playerColor = postResult.message();
                        clientGame.startConnection(clientPostLogin.authToken, postResult.gameID()); // check this!!!
                    }else if (Objects.equals(postResult.message(), "observer")){
                        status = 2;
                        playerColor = null;
                        clientGame.startConnection(clientPostLogin.authToken, postResult.gameID()); //check this!!!
                    }
                    else if(Objects.equals(postResult.message(), "Successful Play")){
                        status = 2;
                    }else if(Objects.equals(postResult.message(), "Successful Observe")){
                        status = 2;
                    }

                }else if(status == 2){
                    gameResult = clientGame.eval(line, playerColor,preLoginResult,postResult.gameID(),game);
                    System.out.println(gameResult);

                    if(Objects.equals(gameResult, "exit")){
                        status = 1;
                    }
                    else if(playerColor == null || Objects.equals(gameResult, "observer")){
                        clientGame.drawChessBoard("observer");
                    }else{
                        status = 2;
//                        clientGame.drawChessBoard(playerColor.toString());
                    }


                }

            } catch (Exception e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    @Override
    public void notify(String message) {
        System.out.println(">>> " + message);

    }
}
