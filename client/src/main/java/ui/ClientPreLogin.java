package ui;

import records.LoginRequest;

import java.util.Arrays;
import java.util.Scanner;
import ServerFacade.ServerFacade;
import records.LoginResult;
import records.RegisterRequest;
import records.RegisterResult;

public class ClientPreLogin {

    ServerFacade serverFacade = new ServerFacade();

    public ClientPreLogin(String serverUrl, Repl repl) {

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
            throw new RuntimeException(e);
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
        LoginResult test = serverFacade.login(request);
        System.out.print(test);
        return "here_login";
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
        RegisterResult test = serverFacade.register(request);
        System.out.print(test);

        return "here_register";
    }

    String help() {

        return "Type the number you wish that coorelates with what you would like to do: \n \n" +
                "1. Register\n" +
                "2. Login\n" +
                "3. Quit\n" +
                "\n" +
                "Request: ";
    }

    private Integer statusChange(){
        return 0;
    }

}
