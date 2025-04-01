package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class DrawChessBoard {

    private static final int HEADERLENGTH = 10;
    private static final int numOfGameRows = 8;
    private static final boolean isNewGame = false;
    public static ChessGame board;

    public DrawChessBoard(ChessGame board){
        this.board = board;
    }

    public static void main(String[] args) throws InvalidMoveException {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String[] moveLocation = {"1","2"};
        //displays white pov
        ChessPosition positionStart = new ChessPosition(2,1);
        ChessPosition positionEnd = new ChessPosition(4,1);
        ChessMove move = new ChessMove(positionStart,positionEnd,null);
        ChessGame testBoard = new ChessGame();
        testBoard.makeMove(move);

        DrawChessBoard drawTest = new DrawChessBoard(testBoard);

        drawTest.drawEntireBoardWhiteSide(out, moveLocation , ChessPiece.PieceType.KING);
        out.println();
        //displays black pov
        drawTest.drawEntireBoardBlackSide(out,moveLocation,ChessPiece.PieceType.KING);

    }

    public static void drawBlankPlayablePartWhite(PrintStream out, String [] playerMove, ChessPiece.PieceType pieceType){
        String color = "white";
        for(int i = numOfGameRows; i > 0 ; i--){
            if(i % 2 == 0){
                drawEvenRow(out, i,playerMove,pieceType,color) ;
            }else{
                drawOddRow(out,i,playerMove,pieceType,color);
            }
        }
    }

    public static void drawBlankPlayablePartBlack(PrintStream out,String [] playerMove, ChessPiece.PieceType pieceType){
        String color = "black";
        for(int i = 1; i < numOfGameRows + 1 ; i++){
            if(i % 2 == 0){
                drawOddRow(out, i ,playerMove,pieceType,color) ;
            }else{
                drawEvenRow(out, i ,playerMove, pieceType,color);
            }
        }
    }

    private static void drawOddRow(PrintStream out, Integer rowNumber, String[] playerInput, ChessPiece.PieceType chessPiece, String color){
        placeOneSquare(out, SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        for(int column = 0; column < numOfGameRows; column++){
            String checkrow = checkRow(out, rowNumber,column, color);
            if(column%2 == 0) {
                out.print(SET_BG_COLOR_BLACK);
                out.print(checkrow);
            }else{
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(checkrow);

            }

        }
        placeOneSquare(out,SET_BG_COLOR_DARK_GREEN,SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }
    private static void drawEvenRow(PrintStream out, Integer rowNumber, String[] playerMove, ChessPiece.PieceType pieceType, String color){
        placeOneSquare(out, SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_WHITE, " " + rowNumber + " ");
        for(int column = 0; column < numOfGameRows; column++){
            String checkrow = checkRow(out, rowNumber,column, color);
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

    private static String checkRow(PrintStream out, Integer rowNumber, int column, String color) {
        if(Objects.equals(color, "black")){
            column = mirroredColumn(column);

            ChessPosition position = new ChessPosition(rowNumber,column);
            ChessPiece piece = board.getBoard().getPiece(position);
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

        }
        if(Objects.equals(color, "white")){

            ChessPosition position = new ChessPosition(rowNumber,column+1);
            ChessPiece piece = board.getBoard().getPiece(position);
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

            }
            return "   ";
        }

    private static int mirroredColumn(int column) {
        column = column +1;
        if(column == 1){
            column = 8;
        }else if(column == 2){
            column = 7;
        }else if(column == 3){
            column = 6;
        }else if(column == 4){
            column = 5;
        }else if (column == 5){
            column = 4;
        }else if (column == 6){
            column = 3;
        }else if(column == 7){
            column = 2;
        }else if(column == 8){
            column = 1;
        }
        return column;
    }


    private static String startingPieces(int column, String color) {
        if(column == 0 || column == 7){
            return " R ";
        } else if(column == 1 || column == 6){
            return " N ";
        } else if(column == 2 || column == 5){
            return " B ";
        } else if(column == 3){
            if (Objects.equals(color, "black")){
                return " K ";
            }else{
                return " Q ";
            }
        } else if(column == 4){
            if(Objects.equals(color, "black")){
                return " Q ";
            }
            return " K ";
        }
        else{
            return "   ";
        }

    }


    public static void drawEntireBoardWhiteSide(PrintStream out, String[] movingPiece, ChessPiece.PieceType pieceType){

        out.print(ERASE_SCREEN);

        String[] headers = {"   "," a ", " b ", " c "," d "," e "," f "," g "," h ", "   " };
        drawHeaders(out, headers);

        drawBlankPlayablePartWhite(out,movingPiece,pieceType);

        drawHeaders(out, headers);

    }
    public static void drawEntireBoardBlackSide(PrintStream out, String[] playerMove, ChessPiece.PieceType pieceType){

        out.print(ERASE_SCREEN);
        String[] headers = {"   "," h ", " g ", " f "," e "," d "," c "," b "," a ", "   " };
        drawHeaders(out, headers);

        drawBlankPlayablePartBlack(out,playerMove, pieceType);

        drawHeaders(out, headers);

    }


    private static void drawHeaders(PrintStream out, String[] headers) {

        setBlack(out);
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
    }

    private static void placeOneSquare(PrintStream out, String colorBlock, String stringColor, String insideVariable){
        out.print(colorBlock);
        out.print(stringColor);
        out.print(insideVariable);
    }
}
