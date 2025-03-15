package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class DrawChessBoard {

    private static final int HEADERLENGTH = 10;
    private static final int NumOfGameRows = 8;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawBlankBoardWhiteSide(out);
        //drawBlankBoardBlackSide(out);

    }
    private static void drawBlankPlayablePartWhite(PrintStream out){

        for(int i = NumOfGameRows; i > 0 ; i--){
            if(i % 2 == 0){
                drawEvenRowWhite(out, Integer.toString(i)) ;
            }else{
                drawOddRowWhite(out,Integer.toString(i));
            }
        }
    }
    private static void drawBlankPlayablePartBlack(PrintStream out){

        for(int i = 1; i < NumOfGameRows + 1 ; i++){
            if(i % 2 == 0){
                drawOddRowWhite(out,Integer.toString(i));
            }else{
                drawEvenRowWhite(out, Integer.toString(i)) ;
            }
        }
    }

    private static void drawOddRowWhite(PrintStream out, String rowNumber){
        placeOneSquare(out, SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        for(int i = 0; i < NumOfGameRows; i++){
            if(i%2 == 0) {
                out.print(SET_TEXT_COLOR_WHITE);
                out.print(SET_BG_COLOR_BLACK);
                out.print("   ");
            }else{
                out.print(SET_TEXT_COLOR_WHITE);
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print("   ");

            }

        }
        placeOneSquare(out,SET_BG_COLOR_DARK_GREEN,SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }
    private static void drawEvenRowWhite(PrintStream out, String rowNumber){
        placeOneSquare(out, SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        for(int i = 0; i < NumOfGameRows; i++){
            if(i%2 == 0) {
                out.print(SET_TEXT_COLOR_WHITE);
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print("   ");
            }else{
                out.print(SET_TEXT_COLOR_WHITE);
                out.print(SET_BG_COLOR_BLACK);
                out.print("   ");
            }
        }
        placeOneSquare(out,SET_BG_COLOR_DARK_GREEN,SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }
    private static void drawBlankBoardWhiteSide(PrintStream out){

        out.print(ERASE_SCREEN);
        drawHeadersWhiteSide(out);

        drawBlankPlayablePartWhite(out);

        drawHeadersWhiteSide(out);

    }
    private static void drawBlankBoardBlackSide(PrintStream out){

        out.print(ERASE_SCREEN);
        drawHeadersBlackSide(out);

        drawBlankPlayablePartBlack(out);

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
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void placeOneSquare(PrintStream out, String colorBlock, String stringColor, String insideVariable){
        out.print(colorBlock);
        out.print(stringColor);
        out.print(insideVariable);
    }
}
