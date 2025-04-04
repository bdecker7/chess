package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import serverFacade.WebsocketCommunicator;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ClientGame {

    Scanner scanner = new Scanner(System.in);
    public ChessGame gameBoard = new ChessGame();
    public DrawChessBoard board = new DrawChessBoard(gameBoard); // if I want to change the game I might have to declare this somewhere else
    WebsocketCommunicator ws = new WebsocketCommunicator();
    String authTokenGame = "";
    Integer gameIDGame;

    public ClientGame(String serverUrl, Repl repl) {
        this.ws = ws;
        this.board = board;
    }

    public String eval(String input, String playerColor, String authToken, Integer gameID) throws IOException {
        authTokenGame = authToken;
        gameIDGame = gameID;
//  Check if I call this here
//        ws.connectClient(authTokenGame,gameIDGame);

        try {
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1" -> reDrawBoard(out, playerColor);
                case "2" -> leaveGame(out,playerColor);
                case "3" -> makeMove(out, playerColor);
                case "4" -> resignGame(out);
                case "5" -> highlightLegalMoves(playerColor,out);
                case "6" -> help();

                default -> help();
            };
        } catch (InvalidRequest | IOException ex) {
            return ex.getMessage();
        }

    }

    private String leaveGame(PrintStream out, String playerColor) throws IOException {


        System.out.println("Are you sure you want to leave? (yes/no) ");
        String responseString = scanner.nextLine();
        if(Objects.equals(responseString, "yes")){
            if(Objects.equals(playerColor, "WHITE")){
                //delete the player from this and bring them back to null.

                ws.leaveWs(authTokenGame,gameIDGame);

            }else if(Objects.equals(playerColor, "BLACK")){
                //delete the black player from game
                ws.leaveWs(authTokenGame,gameIDGame);
            }
            return "exit";
        }
        return "Press 'Enter' for game menu options" ;
    }

    private String highlightLegalMoves(String playerColor, PrintStream out) {

        System.out.println("Row: ");
        String rowString = scanner.nextLine();
        System.out.println("Column: ");
        String columnString = scanner.nextLine();
        try {
            int row = Integer.parseInt(rowString);
            int col = Integer.parseInt(columnString);
            ChessPosition requestedCurrentPosition = new ChessPosition(row,col);
            board.changeHighlightRequest(true);
            if(Objects.equals(playerColor, "WHITE")){
                board.drawEntireBoardWhiteSide(out,null,requestedCurrentPosition);
                board.changeHighlightRequest(false);
            }else if(Objects.equals(playerColor, "BLACK")){
                board.drawEntireBoardBlackSide(out,null,requestedCurrentPosition);
                board.changeHighlightRequest(false);
            }

        } catch (NumberFormatException e) {
            return "Invalid input: row and column must be numbers";
        }

        return "Moveable places are highlighted in blue or magenta";
    }

    private String resignGame(PrintStream out) throws IOException {

        ws.resignWs(authTokenGame,gameIDGame);

        return "resign game";
    }

    private String makeMove(PrintStream out, String playerColor) {
        System.out.println("From Row: ");
        String rowFromString = scanner.nextLine();
        System.out.println("From Column: ");
        String columnFromString = scanner.nextLine();

        System.out.println("To Row: ");
        String rowToString = scanner.nextLine();
        System.out.println("To Column: ");
        String columnToString = scanner.nextLine();
        try {
            int row = Integer.parseInt(rowFromString);
            int col = Integer.parseInt(columnFromString);
            int toRow = Integer.parseInt(rowToString);
            int toCol = Integer.parseInt(columnToString);

            ChessPosition requestedCurrentPosition = new ChessPosition(row,col);
            ChessPosition requestedMovingPosition = new ChessPosition(toRow,toCol);

            // call the Websocket communicator make move function here to make the move
            ChessMove move = new ChessMove(requestedCurrentPosition,requestedMovingPosition,null);

            ws.makeMoveWs(authTokenGame,gameIDGame,move);
            if(Objects.equals(playerColor, "WHITE")){
                board.drawEntireBoardWhiteSide(out,null,null);

            }else if(Objects.equals(playerColor, "BLACK")){
                board.drawEntireBoardBlackSide(out,null,null);

            }

        } catch (NumberFormatException e) {
            return "Invalid input: row and column must be numbers";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "make moves";
    }

    private String reDrawBoard(PrintStream out, String playerColor) {
        if(Objects.equals(playerColor, "WHITE")){
            board.drawEntireBoardWhiteSide(out,null,null);
        }else if(Objects.equals(playerColor, "BLACK")){
            board.drawEntireBoardBlackSide(out,null,null);
        }else{
            board.drawEntireBoardWhiteSide(out,null,null);
        }

        return "Press 'Enter' for game menu";
    }

    public void drawChessBoard(String color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        switch (color) {
//            case "WHITE" -> whiteJoin(out);
//            case "BLACK" -> DrawChessBoard.drawEntireBoardBlackSide(out, null, null);
            case "observer" -> board.drawEntireBoardWhiteSide(out, null, null);
            case null, default -> System.out.println("can't access game");
        }

    }


    private String help() {
        return "1. Re-draw chessboard\n" +
                "2. Leave\n" +
                "3. Make Move\n" +
                "4. Resign\n" +
                "5. Highlight Legal Moves\n" +
                "6. Help\n\n" +
                "Selection: ";
    }
}
