package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class DrawChessBoard {

    private static final int HEADERLENGTH = 10;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);
    }


    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = {"  "," a ", " b ", " c "," d "," e "," f "," g "," h ", "  " };
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
}
