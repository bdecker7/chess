package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import serverFacade.ServerMessageObserver;
import serverFacade.WebsocketCommunicator;

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
    String serverUrl;
    ServerMessageObserver notification;
    Repl repl;

    public ClientGame(String serverUrl, Repl repl) {
        this.ws = ws;
        this.gameBoard = gameBoard;
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    public String eval(String input, String playerColor, String authToken, Integer gameID) throws Exception {
        authTokenGame = authToken;
        gameIDGame = gameID;

        try {

            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1" -> reDrawBoard(out, playerColor);
                case "2" -> leaveGame(out,playerColor);
                case "3" -> makeMove(out, playerColor);
                case "4" -> resignGame(out,playerColor);
                case "5" -> highlightLegalMoves(playerColor,out);
                case "6" -> help();

                default -> help();
            };
        } catch (InvalidRequest | IOException ex) {
            return ex.getMessage();
        }

    }

    private String leaveGame(PrintStream out, String playerColor) throws Exception {


        System.out.println("Are you sure you want to leave? (yes/no) ");
        String responseString = scanner.nextLine();
        if(Objects.equals(responseString, "yes")){
            if(Objects.equals(playerColor, "WHITE")){
                //delete the player from this and bring them back to null.
                ws.webSocketFacade(serverUrl,repl);
                ws.leaveWs(authTokenGame,gameIDGame);

            }else if(Objects.equals(playerColor, "BLACK")){
                //delete the black player from game
                ws.webSocketFacade(serverUrl,repl);
                ws.leaveWs(authTokenGame,gameIDGame);
            }else if(Objects.equals(playerColor, "observer")){
                ws.webSocketFacade(serverUrl,repl);
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

    private String resignGame(PrintStream out, String playerColor) throws Exception {
        if(!playerColor.equals("observer")){
            ws.webSocketFacade(serverUrl,repl);
            ws.resignWs(authTokenGame,gameIDGame);
            return "resign game";
        }
        return "observer can't resign game";
    }

    private String makeMove(PrintStream out, String playerColor) throws Exception {

        if(!playerColor.equals("observer")) {
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
                int col = convertToNumber(columnFromString);
//            int col = Integer.parseInt(columnFromString);
                int toRow = Integer.parseInt(rowToString);
                int toCol = convertToNumber(columnToString);
//            int toCol = Integer.parseInt(columnToString);

                ChessPosition requestedCurrentPosition = new ChessPosition(row, col);
                ChessPosition requestedMovingPosition = new ChessPosition(toRow, toCol);

                // call the Websocket communicator make move function here to make the move
                ChessMove move = new ChessMove(requestedCurrentPosition, requestedMovingPosition, null);

                ws.webSocketFacade(serverUrl,repl);
                ws.makeMoveWs(authTokenGame, gameIDGame, move);
//                board = ws.gameData.game();

                // need to check if move is valid.
                if (Objects.equals(playerColor, "WHITE")) {
//                    board.drawEntireBoardWhiteSide(out, requestedMovingPosition, requestedCurrentPosition);
//                    board = new DrawChessBoard(ws.gameData.game());
                    board.drawEntireBoardWhiteSide(out, null,null);
                } else if (Objects.equals(playerColor, "BLACK")) {
//                    board.drawEntireBoardBlackSide(out, requestedMovingPosition, requestedCurrentPosition);
//                    board = new DrawChessBoard(ws.gameData.game());
                    board.drawEntireBoardWhiteSide(out, null,null);
                }

            } catch (NumberFormatException e) {
                return "Invalid input: row and column must be valid";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return "make moves";
        }else{
            throw new Exception("observer can't make moves");
        }
    }

    private Integer convertToNumber(String columnFromString) throws Exception {
        if(columnFromString.equals("a")){
            return 1;
        }else if(columnFromString.equals("b")){
            return 2;
        }else if(columnFromString.equals("c")){
            return 3;
        }else if(columnFromString.equals("d")){
            return 4;
        }else if (columnFromString.equals("e")){
            return 5;
        }else if(columnFromString.equals("f")){
            return 6;
        }else if(columnFromString.equals("g")){
            return 7;
        }else if(columnFromString.equals("h")){
            return 8;
        }else{
            throw new Exception("Invalid color");
        }
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
