package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import serverfacade.ServerMessageObserver;
import serverfacade.WebsocketCommunicator;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ClientGame {

    Scanner scanner = new Scanner(System.in);
    public static ChessGame gameBoard; //maybe it is because I'm calling a new game?
    public static DrawChessBoard boardDrawer = new DrawChessBoard(); // if I want to change the game I might have to declare this somewhere else
    WebsocketCommunicator ws = new WebsocketCommunicator(gameBoard);
    String authTokenGame = "";
    Integer gameIDGame;
    String serverUrl;
    ServerMessageObserver notification;
    Repl repl;
    static String color = "";

    public ClientGame(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    public static void setCurrentGame(GameData game) {
        gameBoard = game.game();
        if (Objects.equals(color, "WHITE")){
            boardDrawer.drawEntireBoardWhiteSide(System.out,gameBoard);
        }else if(Objects.equals(color, "BLACK")){
            boardDrawer.drawEntireBoardBlackSide(System.out,gameBoard);
        }else{
            boardDrawer.drawEntireBoardWhiteSide(System.out,gameBoard);
        }
    }

    public void startConnection(String auth, Integer gameID, String playerColor) throws Exception {
        color = playerColor;
        ws.webSocketFacade(serverUrl,repl);
        ws.connectClient(auth, gameID);
    }

    public String eval(String input, String playerColor, String authToken, Integer gameID) throws Exception {
        authTokenGame = authToken;
        gameIDGame = gameID;
        color = playerColor;

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
            }else if(Objects.equals(playerColor, "observer") || playerColor == null){
                ws.webSocketFacade(serverUrl,repl);
                ws.leaveWs(authTokenGame,gameIDGame);
            }
            return "exit";
        }else{
            return "Press 'Enter' for game menu options";
        }
    }

    private String highlightLegalMoves(String playerColor, PrintStream out) {

        System.out.println("Requested Position: ");
        String moveString = scanner.nextLine();
        String columnString = moveString.substring(0,1);
        String rowString = moveString.substring(1);
        try {
            int row = Integer.parseInt(rowString);
            int col = convertToNumber(columnString);
            ChessPosition requestedCurrentPosition = new ChessPosition(row,col);
            boardDrawer.changeHighlightRequest(true,requestedCurrentPosition);
            if(Objects.equals(playerColor, "WHITE")){
                boardDrawer.drawEntireBoardWhiteSide(out,gameBoard);
                boardDrawer.changeHighlightRequest(false, null);
            }else if(Objects.equals(playerColor, "BLACK")){
                boardDrawer.drawEntireBoardBlackSide(out,gameBoard);
                boardDrawer.changeHighlightRequest(false, null);
            }else{
                boardDrawer.drawEntireBoardWhiteSide(out,gameBoard);
                boardDrawer.changeHighlightRequest(false, null);
            }

        } catch (Exception e) {
            return "Invalid input: row and column must be on board";
        }

        return "Moveable places are highlighted in blue or magenta";
    }

    private String resignGame(PrintStream out, String playerColor) throws Exception {

        if(playerColor == null){
            System.out.println("Error: observer can't resign game");
        }
        else if(!playerColor.equals("observer")){
            System.out.println("Are you sure you want to resign? (yes/no) ");
            String responseString = scanner.nextLine();
            if(Objects.equals(responseString, "yes")){
                if(Objects.equals(playerColor, "WHITE")){
                    ws.webSocketFacade(serverUrl,repl);
                    ws.resignWs(authTokenGame,gameIDGame);
                    return "exit";
                }else if(Objects.equals(playerColor, "BLACK")){
                    ws.webSocketFacade(serverUrl,repl);
                    ws.resignWs(authTokenGame,gameIDGame);
                    return "exit";
                }
        }else{
            return "Press 'Enter' for menu";
        }
        }return "";
    }

    private String makeMove(PrintStream out, String playerColor) throws Exception {

        if(playerColor != null && !playerColor.equals("observer")) {
            if(gameBoard.isInStalemate(ChessGame.TeamColor.WHITE)||gameBoard.isInStalemate(ChessGame.TeamColor.BLACK)){
                return "Stalemate! Game over. No more moves can be made!";
            }
            else if(gameBoard.isInCheckmate(ChessGame.TeamColor.WHITE)||gameBoard.isInCheckmate(ChessGame.TeamColor.BLACK)){
                return "Checkmate! Game over. No more moves can be made!";
            }else if((gameBoard.getTeamTurn() != ChessGame.TeamColor.WHITE && playerColor.equals("WHITE"))
                    || (playerColor.equals("BLACK")&& gameBoard.getTeamTurn() != ChessGame.TeamColor.BLACK )){
                return "Not your turn, can't make that move";
            }else if((gameBoard.getIsResigned())){
                return "game resigned, can't make any more moves";
            }
//            else if((gameBoard.isInCheck(ChessGame.TeamColor.BLACK))||gameBoard.isInCheck(ChessGame.TeamColor.WHITE)){
//                return "CHECK!!";
//            }

            System.out.println("From Position: ");
            String moveString = scanner.nextLine();
            String columnFromString = moveString.substring(0,1);
            String rowFromString = moveString.substring(1);

            System.out.println("To Position: ");
            String movedString = scanner.nextLine();
            String columnToString = movedString.substring(0,1);
            String rowToString = movedString.substring(1);

            try {
                int row = Integer.parseInt(rowFromString);
                int col = convertToNumber(columnFromString);
                int toRow = Integer.parseInt(rowToString);
                int toCol = convertToNumber(columnToString);

                ChessPosition requestedCurrentPosition = new ChessPosition(row, col);
                ChessPosition requestedMovingPosition = new ChessPosition(toRow, toCol);

                if(gameBoard.getBoard().getPiece(requestedCurrentPosition) == null){
                    return "not piece at 'from' position";
                }
                //Checks promotions
                ChessMove move;
                ChessPiece currentSelectedPiece = gameBoard.getBoard().getPiece(requestedCurrentPosition);
                if(currentSelectedPiece.getPieceType() == ChessPiece.PieceType.PAWN){
                    if(requestedMovingPosition.getRow() == 1 && currentSelectedPiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)){
                        System.out.println("Promotion piece (in lowercase) : ");
                        String promotionPieceString = scanner.nextLine();
                        //check here if the piece is a promotion piece

                        ChessPiece.PieceType piece = getPieceType(promotionPieceString);
                        move = new ChessMove(requestedCurrentPosition, requestedMovingPosition, piece);
                    }else if(requestedMovingPosition.getRow() == 8 && currentSelectedPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)){
                        System.out.println("Promotion piece (in lowercase) : ");
                        String promotionPieceString = scanner.nextLine();

                        ChessPiece.PieceType piece = getPieceType(promotionPieceString);
                        move = new ChessMove(requestedCurrentPosition, requestedMovingPosition, piece);
                    }else{
                        move = new ChessMove(requestedCurrentPosition, requestedMovingPosition, null);
                    }
                    move = new ChessMove(requestedCurrentPosition, requestedMovingPosition, null);
                }else{
                    move = new ChessMove(requestedCurrentPosition, requestedMovingPosition, null);
                }
                ws.webSocketFacade(serverUrl,repl);
                ws.makeMoveWs(authTokenGame, gameIDGame, move);

            } catch (NumberFormatException e) {
                return "Invalid input: row and column must be valid";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "made move";
        }else{
//            throw new Exception("observer can't make moves");
            return "observer can't make moves";
        }
    }

    private static ChessPiece.PieceType getPieceType(String promotionPieceString) {
        ChessPiece.PieceType piece;
        if(Objects.equals(promotionPieceString, "bishop")){
            piece = ChessPiece.PieceType.BISHOP;
        }else if(Objects.equals(promotionPieceString, "rook")){
            piece = ChessPiece.PieceType.ROOK;
        }else if(Objects.equals(promotionPieceString, "knight")){
            piece = ChessPiece.PieceType.KNIGHT;
        }else if(Objects.equals(promotionPieceString, "queen")){
            piece = ChessPiece.PieceType.QUEEN;
        }else{
            piece = null;
        }
        return piece;
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
            boardDrawer.drawEntireBoardWhiteSide(out,gameBoard);
        }else if(Objects.equals(playerColor, "BLACK")){
            boardDrawer.drawEntireBoardBlackSide(out,gameBoard);
        }else{
            boardDrawer.drawEntireBoardWhiteSide(out,gameBoard);
        }

        return "Press 'Enter' for game menu";
    }

    public void drawChessBoard(String color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        switch (color) {
            case "WHITE", "observer" -> {
                boardDrawer.drawEntireBoardWhiteSide(out, gameBoard);
            }
            case "BLACK" -> {
                boardDrawer.drawEntireBoardBlackSide(out,gameBoard);
            }
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
