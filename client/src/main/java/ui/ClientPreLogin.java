package ui;

import records.LoginRequest;

import java.util.Arrays;
import java.util.Scanner;
import serverFacade.ServerFacade;
import records.LoginResult;
import records.RegisterRequest;
import records.RegisterResult;
import serverFacade.WebsocketCommunicator;

public class ClientPreLogin {

    ServerFacade serverFacade = new ServerFacade(8080);
    private String authToken;
    String serverUrl;
    public ClientPreLogin(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;

    }

    public String eval(String input ) {

        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1" -> register(params);
                case "2" -> login(params);
                case "3" -> "quit";
                default -> help();
            };
        } catch (InvalidRequest ex) {
            return ex.getMessage();
        } catch (Exception e) {
            return "";
        }
    }

    private String login(String[] params) throws Exception {
        //call serverfacade class
        System.out.println("Username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        LoginRequest request = new LoginRequest(username,password);
        LoginResult result = serverFacade.login(request);

        if(result != null){
            System.out.println("Welcome "+ result.username() + "!");
            authToken = result.authToken();
            return result.authToken();
        }

        return null;

    }

    private String register(String[] params) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();

        RegisterRequest request = new RegisterRequest(username,password,email);
        RegisterResult result = serverFacade.register(request);
        if(result != null){
            System.out.println("Welcome "+ result.username() + "!");
            return result.authToken();
        }
        return null;
    }

    String help() {

        return "Type the number you wish that coorelates with what you would like to do: \n \n" +
                "1. Register\n" +
                "2. Login\n" +
                "3. Quit\n" +
                "4. Help\n" +
                "\n" +
                "Request: ";
    }


}
