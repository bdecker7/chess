package ui;

import chess.ChessGame;
import serverFacade.WebsocketCommunicator;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ClientGame {
    ChessGame gameBoard = new ChessGame();
    DrawChessBoard board = new DrawChessBoard(gameBoard);
    WebsocketCommunicator ws = new WebsocketCommunicator();

    public ClientGame(String serverUrl, Repl repl) {
        this.ws = ws;
    }

    public String eval(String input ) {

        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1" -> reDrawBoard();
                case "2" -> "exit";
                case "3" -> makeMove(params);
                case "4" -> resignGame();
                case "5" -> highlightLegalMoves();
                case "6" -> help();

                default -> help();
            };
        } catch (InvalidRequest ex) {
            return ex.getMessage();
        }

    }

    private String highlightLegalMoves() {
        return "highlight legal moves";
    }

    private String resignGame() {
        return "resign game";
    }

    private String makeMove(String[] params) {
        return "make moves";
    }

    private String reDrawBoard() {
        return "redraw that board sonnnnn";
    }

    public void drawChessBoard(String color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        switch (color) {
            case "WHITE" -> whiteJoin(out);
            case "BLACK" -> DrawChessBoard.drawEntireBoardBlackSide(out, null, null);
            case "observer" -> DrawChessBoard.drawEntireBoardWhiteSide(out, null, null);
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
