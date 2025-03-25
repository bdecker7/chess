package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class ClientGame {

//    DrawChessBoard board = new DrawChessBoard();

    public ClientGame(String serverUrl, Repl repl) {
    }

    public String eval(String input ) {

        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "move" -> "do move function here";
                case "exit" -> "exit";

                default -> help();
            };
        } catch (InvalidRequest ex) {
            return ex.getMessage();
        }

    }

    public void drawChessBoard(String color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        switch (color) {
            case "WHITE" -> DrawChessBoard.drawEntireBoardWhiteSide(out, null, null);
            case "BLACK" -> DrawChessBoard.drawEntireBoardBlackSide(out, null, null);
            case "observer" -> DrawChessBoard.drawEntireBoardWhiteSide(out, null, null);
            case null, default -> System.out.println("can't access game");
        }

    }

    private String help() {
        return "Type 'exit' to leave game";
    }
}
