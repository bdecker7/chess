package ui;

import chess.ChessGame;
import chess.ChessPosition;
import serverFacade.WebsocketCommunicator;

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

    public ClientGame(String serverUrl, Repl repl) {
        this.ws = ws;
        this.board = board;
    }

    public String eval(String input, String playerColor) {

        try {
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1" -> reDrawBoard(out, playerColor);
                case "2" -> "exit";
                case "3" -> makeMove(params);
                case "4" -> resignGame(out);
                case "5" -> highlightLegalMoves(playerColor,out);
                case "6" -> help();

                default -> help();
            };
        } catch (InvalidRequest ex) {
            return ex.getMessage();
        }

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
            // figure out how to draw the highlighted colors
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


        return " ";
    }

    private String resignGame(PrintStream out) {
        return "resign game";
    }

    private String makeMove(String[] params) {
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

        return " ";
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

    private void whiteJoin(PrintStream out) {

        Scanner scanner = new Scanner(System.in);
        ws.connectClient();

        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine();


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
