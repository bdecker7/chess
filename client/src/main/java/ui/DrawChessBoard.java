package ui;

import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class DrawChessBoard {

    private static final int HEADERLENGTH = 10;
    private static final int NumOfGameRows = 8;
    private static final boolean isNewGame = true;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String[] moveLocation = {"1","2"};
        drawBlankBoardWhiteSide(out, moveLocation , ChessPiece.PieceType.KING);
        out.println();
        drawBlankBoardBlackSide(out,moveLocation,ChessPiece.PieceType.KING);


    }
    private static void drawBlankPlayablePartWhite(PrintStream out, String [] playerMove, ChessPiece.PieceType pieceType){

        for(int i = NumOfGameRows; i > 0 ; i--){
            if(i % 2 == 0){
                drawEvenRowWhite(out, Integer.toString(i),playerMove,pieceType) ;
            }else{
                drawOddRowWhite(out,Integer.toString(i),playerMove,pieceType);
            }
        }
    }
    private static void drawBlankPlayablePartBlack(PrintStream out,String [] playerMove, ChessPiece.PieceType pieceType){

        for(int i = 1; i < NumOfGameRows + 1 ; i++){
            if(i % 2 == 0){
                drawEvenRowWhite(out, Integer.toString(i),playerMove,pieceType) ;
            }else{
                drawOddRowWhite(out,Integer.toString(i),playerMove, pieceType);
            }
        }
    }

    private static void drawOddRowWhite(PrintStream out, String rowNumber, String[] playerInput, ChessPiece.PieceType chessPiece){
        placeOneSquare(out, SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        for(int column = 0; column < NumOfGameRows; column++){
            String checkrow = checkRow(out, rowNumber,column);
            if(column%2 == 0) {
//                out.print(SET_TEXT_COLOR_WHITE);
                out.print(SET_BG_COLOR_BLACK);
                out.print(checkrow);
            }else{
//                out.print(SET_TEXT_COLOR_WHITE);
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(checkrow);

            }

        }
        placeOneSquare(out,SET_BG_COLOR_DARK_GREEN,SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }
    private static void drawEvenRowWhite(PrintStream out, String rowNumber, String[] playerMove, ChessPiece.PieceType pieceType){
        placeOneSquare(out, SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        for(int column = 0; column < NumOfGameRows; column++){
            String checkrow = checkRow(out, rowNumber,column);
            if(column%2 == 0) {
                out.print(SET_BG_COLOR_LIGHT_GREY);

                out.print(checkrow);

            }else{
                out.print(SET_BG_COLOR_BLACK);
                out.print(checkrow);
            }
        }
        placeOneSquare(out,SET_BG_COLOR_DARK_GREEN,SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }

    private static String checkRow(PrintStream out, String rowNumber, int column) {
        if(isNewGame){
            if(Objects.equals(rowNumber, "1")){
                    out.print(SET_TEXT_COLOR_YELLOW);
                    return startingPieces(column);

            }else if(Objects.equals(rowNumber, "8")){
                    out.print(SET_TEXT_COLOR_RED);
                    return startingPieces(column);

            }else if(Objects.equals(rowNumber, "8")){
                out.print(SET_TEXT_COLOR_RED);
                return startingPieces(column);
            }


            else if(Objects.equals(rowNumber, "2")){
                out.print(SET_TEXT_COLOR_YELLOW);
                return " P ";
            } else if (Objects.equals(rowNumber, "7")) {
                out.print(SET_TEXT_COLOR_RED);
                return " P ";

            }
        }else{
            return "   ";
        }
        return "   ";
    }

    private static String startingPieces(int column) {
        if(column == 0 || column == 7){
            return " R ";
        } else if(column == 1 || column == 6){
            return " N ";
        } else if(column == 2 || column == 5){
            return " B ";
        } else if(column == 3){
            return " Q ";
        } else if(column == 4){
            return " K ";
        }
        else{
            return "   ";
        }

    }

    private static void playerSetUp(PrintStream out,int row) {
        if(row == 1){
            String[] startingLineUp = {" R ", " N ", " B "," Q "," K "," B "," N "," R "};
            for (int boardCol = 0; boardCol < 8 ; ++boardCol) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(startingLineUp[boardCol]);
            }

        }else if(row == 2){
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(" P ");
        }

    }

    private static void drawBlankBoardWhiteSide(PrintStream out, String[] movingPiece, ChessPiece.PieceType pieceType){

        out.print(ERASE_SCREEN);
        drawHeadersWhiteSide(out);

        drawBlankPlayablePartWhite(out,movingPiece,pieceType);

        drawHeadersWhiteSide(out);

    }
    private static void drawBlankBoardBlackSide(PrintStream out, String[] playerMove, ChessPiece.PieceType pieceType){

        out.print(ERASE_SCREEN);
        drawHeadersBlackSide(out);

        drawBlankPlayablePartBlack(out,playerMove, pieceType);

        drawHeadersBlackSide(out);

    }


    private static void drawHeadersWhiteSide(PrintStream out) {

        setBlack(out);

        String[] headers = {"   "," a ", " b ", " c "," d "," e "," f "," g "," h ", "   " };
        for (int boardCol = 0; boardCol < HEADERLENGTH ; ++boardCol) {
            printHeaderText(out, headers[boardCol]);
        }
        out.println();
    }
    private static void drawHeadersBlackSide(PrintStream out) {

        setBlack(out);

        String[] headers = {"   "," h ", " g ", " f "," e "," d "," c "," b "," a ", "   " };
        for (int boardCol = 0; boardCol < HEADERLENGTH ; ++boardCol) {
            printHeaderText(out, headers[boardCol]);
        }
        out.println();
    }

    private static void printHeaderText(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(headerText);

        setBlack(out);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        //out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void placeOneSquare(PrintStream out, String colorBlock, String stringColor, String insideVariable){
        out.print(colorBlock);
        out.print(stringColor);
        out.print(insideVariable);
    }
}
