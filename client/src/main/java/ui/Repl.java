package ui;

import java.util.Objects;
import java.util.Scanner;

public class Repl {

    private final ClientPreLogin clientPreLogin;
    private final ClientPostLogin clientPostLogin;
    private final ClientGame clientGame;
    private static Integer status = 0;
    private String preLoginResult = null;
    private String postResult = null;
    private String playerColor = null;
    private String gameResult = null;

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
                    System.out.println(postResult);

                    if(Objects.equals(postResult, "logged out")){
                        status = 0;
                    }else if(Objects.equals(postResult, "WHITE") || Objects.equals(postResult, "BLACK")){
                        status = 2;
                        playerColor = postResult;
                    }else if (Objects.equals(postResult, "observer")){
                        status = 2;
                        playerColor = null;
                    }
                    else if(Objects.equals(postResult, "Successful Play")){
                        status = 2;
                    }else if(Objects.equals(postResult, "Successful Observe")){
                        status = 2;
                    }

                }else if(status == 2){
                    gameResult = clientGame.eval(line, playerColor);
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

}
