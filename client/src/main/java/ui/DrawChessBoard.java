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
        drawBlankBoard(out);

    }
    private static void drawBlankPlayablePart(PrintStream out){

        for(int i = 1; i < NumOfGameRows + 1; i++){
            if(i % 2 == 0){
                drawOddRow(out,Integer.toString(i));
            }else{
                drawEvenRow(out, Integer.toString(i)) ;
            }

        }



    }

    private static void drawOddRow(PrintStream out, String rowNumber){
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
    private static void drawEvenRow(PrintStream out, String rowNumber){
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
    private static void drawBlankBoard(PrintStream out){

        out.print(ERASE_SCREEN);
        drawHeadersWhiteSide(out);

        drawBlankPlayablePart(out);


        drawHeadersWhiteSide(out);

    }


    private static void drawHeadersWhiteSide(PrintStream out) {

        setBlack(out);

        String[] headers = {"   "," a ", " b ", " c "," d "," e "," f "," g "," h ", "   " };
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
