package ui;

import java.util.Objects;
import java.util.Scanner;

public class Repl {

    private final ClientPreLogin clientPreLogin;
    private final ClientPostLogin clientPostLogin;
    private final ClientGame clientGame;
    private static Integer status = 0;

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
                    result = clientPreLogin.eval(line);
                    if(Objects.equals(result, "Successful Register")){
                        status = 1;
                    }else if(Objects.equals(result, "Successful Login")){
                        status = 1;
                    }

                }else if(status == 1) {
                    result = clientPostLogin.evalPost(line);
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

                }
                System.out.print(result);
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
