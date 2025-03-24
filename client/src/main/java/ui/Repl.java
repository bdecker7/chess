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
            printPrompt();
            String line = scanner.nextLine();

            try {
                if(status == 0){
                    preLoginResult = clientPreLogin.eval(line);
                    if(preLoginResult != null){
                        status = 1;
                    }

                }else if(status == 1) {
                    postResult = clientPostLogin.evalPost(line, preLoginResult);
                    System.out.println(postResult);

                    if(Objects.equals(result, "logout")){
                        status = 0;
                    }else if(Objects.equals(result, "Successful Join")){
                        status = 2;
                    }else if(Objects.equals(result, "Successful Play")){
                        status = 2;
                    }else if(Objects.equals(result, "Successful Observe")){
                        status = 2;
                    }

                }else if(status == 2){
                    result = clientGame.eval(line);
                    System.out.println(result);
                }
//                System.out.print(postResult);
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
