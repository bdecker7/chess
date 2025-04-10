package ui;

import chess.*;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class DrawChessBoard {

    private static final int HEADERLENGTH = 10;
    private static final int NUM_OF_GAME_ROWS = 8;
    public static boolean highlightRequest = false;
    public static ChessGame board;
//    public PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public DrawChessBoard(ChessGame board){
        this.board = board;
    }

//    public static void main(String[] args) throws InvalidMoveException {
//        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//        String[] moveLocation = {"1","2"};
//        //displays white pov
//        ChessPosition positionStart = new ChessPosition(2,1);
//        ChessPosition positionEnd = new ChessPosition(4,1);
//        ChessMove move = new ChessMove(positionStart,positionEnd,null);
//        ChessGame testBoard = new ChessGame();
//        testBoard.makeMove(move);
//        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
//
//
//        DrawChessBoard drawTest = new DrawChessBoard(testBoard);
//
//        drawTest.drawEntireBoardWhiteSide(out,null, null);
//        out.println();
//        //displays black pov
//        drawTest.drawEntireBoardBlackSide(out,null,null);
//
//    }
    public void changeHighlightRequest(boolean request){
        highlightRequest = request;
    }
    public String isHighlightPlayerMoves(Integer row, Integer column, ChessPosition requestedCurrentPosition){

        ChessPosition position = new ChessPosition(row,column+1);
        Collection<ChessMove> movesList = board.validMoves(requestedCurrentPosition);

        if (requestedCurrentPosition.equals(position)){
            return "requested piece";
        }
        for(ChessMove moves : movesList){
            if (Objects.equals(moves.getEndPosition(), position)) {
                return "highlighted";
            }
        }
        return "not highlighted";
    }

    public void drawBlankPlayablePartWhite(PrintStream out, ChessPosition playerMove, ChessPosition requestedCurrentPosition){
        String color = "white";
        for(int i = NUM_OF_GAME_ROWS; i > 0 ; i--){
            if(i % 2 == 0){
                drawEvenRow(out, i,playerMove,requestedCurrentPosition,color) ;
            }else{
                drawOddRow(out,i,playerMove,requestedCurrentPosition,color);
            }
        }
    }

    public void drawBlankPlayablePartBlack(PrintStream out,ChessPosition playerMove, ChessPosition requestedCurrentPosition){
        String color = "black";
        for(int i = 1; i < NUM_OF_GAME_ROWS + 1 ; i++){
            if(i % 2 == 0){
                drawOddRow(out, i ,playerMove,requestedCurrentPosition,color) ;
            }else{
                drawEvenRow(out, i ,playerMove, requestedCurrentPosition,color);
            }
        }
    }

    private void drawOddRow(PrintStream out, Integer rowNumber, ChessPosition playerMove, ChessPosition requestedCurrentPosition, String color){
        placeOneSquare(out, SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        for(int column = 0; column < NUM_OF_GAME_ROWS; column++){
            String checkrow = checkRow(out, rowNumber,column, color);
            if(column%2 == 0) {
                int actualColumn;
                if(Objects.equals(color, "black")){
                    actualColumn = mirroredColumn(column)-1;

                }else{
                    actualColumn = column;
                }
                if(requestedCurrentPosition != null){
                    if(isHighlightPlayerMoves(rowNumber,actualColumn,requestedCurrentPosition).equals("highlighted")) {
                        out.print(SET_BG_COLOR_BLUE);
                    }else if(isHighlightPlayerMoves(rowNumber,actualColumn,requestedCurrentPosition).equals("requested piece")){
                        out.print(SET_BG_COLOR_WHITE);
                    }else{
                        out.print(SET_BG_COLOR_BLACK);
                    }
                } else{
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print(checkrow);
            }else{
                int actualColumn;
                if(Objects.equals(color, "black")){
                    actualColumn = mirroredColumn(column)-1;
                }else{
                    actualColumn = column;
                }

                if(requestedCurrentPosition != null){
                    if(isHighlightPlayerMoves(rowNumber,actualColumn,requestedCurrentPosition).equals("highlighted")) {
                        out.print(SET_BG_COLOR_MAGENTA);
                    }else if(isHighlightPlayerMoves(rowNumber,actualColumn,requestedCurrentPosition).equals("requested piece")){
                        out.print(SET_BG_COLOR_WHITE);
                    }else{
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                }else{
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                out.print(checkrow);
            }

        }
        placeOneSquare(out,SET_BG_COLOR_DARK_GREEN,SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }
    private void drawEvenRow(PrintStream out, Integer rowNumber, ChessPosition playerMove, ChessPosition requestedCurrentPosition, String color){
        placeOneSquare(out, SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        for(int column = 0; column < NUM_OF_GAME_ROWS; column++){
            String checkrow = checkRow(out, rowNumber,column, color);
            if(column%2 == 0) {
                int actualColumn;
                if(Objects.equals(color, "black")){
                    actualColumn = mirroredColumn(column)-1;
                }else{
                    actualColumn = column;
                }

                if(requestedCurrentPosition != null){
                    if(isHighlightPlayerMoves(rowNumber,actualColumn,requestedCurrentPosition).equals("highlighted")) {
                        out.print(SET_BG_COLOR_MAGENTA);
                    }else if(isHighlightPlayerMoves(rowNumber,actualColumn,requestedCurrentPosition).equals("requested piece")){
                        out.print(SET_BG_COLOR_WHITE);
                    }else{
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    }

                }else{
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                out.print(checkrow);

            }else{
                int actualColumn;
                if(Objects.equals(color, "black")){
                    actualColumn = mirroredColumn(column)-1;
                }else{
                    actualColumn = column;
                }
                if(requestedCurrentPosition != null){
                    if(isHighlightPlayerMoves(rowNumber,actualColumn,requestedCurrentPosition).equals("highlighted")) {
                        out.print(SET_BG_COLOR_BLUE);
                    }else if(isHighlightPlayerMoves(rowNumber,actualColumn,requestedCurrentPosition).equals("requested piece")){
                        out.print(SET_BG_COLOR_WHITE);
                    }else{
                        out.print(SET_BG_COLOR_BLACK);
                    }
                }else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print(checkrow);
            }
        }
        placeOneSquare(out,SET_BG_COLOR_DARK_GREEN,SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }

    private static String checkRow(PrintStream out, Integer rowNumber, int column, String color) {
        if(Objects.equals(color, "black")){
            column = mirroredColumn(column);

            ChessPosition position = new ChessPosition(rowNumber,column);
            ChessPiece piece = board.getBoard().getPiece(position);
            String spaces = getPieceString(out, piece);
            if (spaces != null) {
                return spaces;
            }

        }
        else if(Objects.equals(color, "white")){

            ChessPosition position = new ChessPosition(rowNumber,column+1);
            ChessPiece piece = board.getBoard().getPiece(position);
            String spaces = getPieceString(out, piece);
            if (spaces != null) {
                return spaces;
            }

        }
            return "   ";
        }

    private static String getPieceString(PrintStream out, ChessPiece piece) {
        if(piece == null){
            return "   ";
        } else if(piece.getPieceType() == ChessPiece.PieceType.KING){
            if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(SET_TEXT_COLOR_RED);
                return " K ";
            }else{
                out.print(SET_TEXT_COLOR_YELLOW);
                return " K ";
            }
        }else if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
            if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(SET_TEXT_COLOR_RED);
                return " Q ";
            }else{
                out.print(SET_TEXT_COLOR_YELLOW);
                return " Q ";
            }
        }else if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(SET_TEXT_COLOR_RED);
                return " P ";
            }else{
                out.print(SET_TEXT_COLOR_YELLOW);
                return " P ";
            }
        }else if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(SET_TEXT_COLOR_RED);
                return " N ";
            }else{
                out.print(SET_TEXT_COLOR_YELLOW);
                return " N ";
            }
        }else if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
            if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(SET_TEXT_COLOR_RED);
                return " R ";
            }else{
                out.print(SET_TEXT_COLOR_YELLOW);
                return " R ";
            }
        }else if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(SET_TEXT_COLOR_RED);
                return " B ";
            }else{
                out.print(SET_TEXT_COLOR_YELLOW);
                return " B ";
            }
        }
        return null;
    }

    private static int mirroredColumn(int column) {
        column = column + 1;
        int reversedColumn = column;
        if(column == 1){
            reversedColumn = 8;
        }else if(column == 2){
            reversedColumn = 7;
        }else if(column == 3){
            reversedColumn = 6;
        }else if(column == 4){
            reversedColumn = 5;
        }else if (column == 5){
            reversedColumn = 4;
        }else if (column == 6){
            reversedColumn = 3;
        }else if(column == 7){
            reversedColumn = 2;
        }else if(column == 8){
            reversedColumn = 1;
        }
        return reversedColumn;
    }



    public void drawEntireBoardWhiteSide(PrintStream out, ChessPosition movingPosition, ChessPosition requestedCurrentPosition){

        out.print(ERASE_SCREEN);

        String[] headers = {"   "," a ", " b ", " c "," d "," e "," f "," g "," h ", "   " };
        drawHeaders(out, headers);

        drawBlankPlayablePartWhite(out,movingPosition,requestedCurrentPosition);

        drawHeaders(out, headers);

    }
    public void drawEntireBoardBlackSide(PrintStream out, ChessPosition movingPosition, ChessPosition requestedCurrentPosition){

        out.print(ERASE_SCREEN);
        String[] headers = {"   "," h ", " g ", " f "," e "," d "," c "," b "," a ", "   " };
        drawHeaders(out, headers);

        drawBlankPlayablePartBlack(out,movingPosition, requestedCurrentPosition);

        drawHeaders(out, headers);

    }


    private void drawHeaders(PrintStream out, String[] headers) {

        setBlack(out);
        for (int boardCol = 0; boardCol < HEADERLENGTH ; ++boardCol) {
            printHeaderText(out, headers[boardCol]);
        }
        out.println();
    }

    private void printHeaderText(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(headerText);

        setBlack(out);
    }

    private void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private void placeOneSquare(PrintStream out, String colorBlock, String stringColor, String insideVariable){
        out.print(colorBlock);
        out.print(stringColor);
        out.print(insideVariable);
    }
}
