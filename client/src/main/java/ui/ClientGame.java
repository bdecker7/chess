package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClientGame {

//    DrawChessBoard board = new DrawChessBoard();

    public ClientGame(String serverUrl, Repl repl) {
    }

//    public String eval(String input ) {
//
////        try {
////            var tokens = input.toLowerCase().split(" ");
////            var cmd = (tokens.length > 0) ? tokens[0] : "help";
////            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
////            return switch (cmd) {
////                case "1" -> drawChessBoard();
////                case "2" -> "quit";
////                default -> help();
////            };
////        } catch (InvalidRequest ex) {
////            return ex.getMessage();
////        }
//
//    }

    public void drawChessBoard(String color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if(color == "WHITE"){
            DrawChessBoard.drawEntireBoardWhiteSide(out,null,null);
        }else if(color == "BLACK"){
            DrawChessBoard.drawEntireBoardBlackSide(out,null,null);
        }else if(color == "observer"){

            DrawChessBoard.drawEntireBoardWhiteSide(out,null,null);
        }else{
            System.out.println("can't access game");
        }

    }


    private String help() {
        return "game_help";
    }
}
