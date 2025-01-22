package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class BishopMoves implements PieceMovesCalculator {

    private int x = 1;
    private final ChessPosition currentPosition;
    private final ChessBoard board;
    public BishopMoves(ChessBoard board, ChessPosition currentPosition){
        this.currentPosition = currentPosition;
        this.board = board;
    }
    @Override
    public ArrayList<ChessMove> validMove() {
        //moves diagonal. From the initial position,
        // to move right up, x position goes up and y position goes up by 1
        // to move right down, x position goes down by 1 and y position goes up by 1
        // to move left up, x position goes up by 1 and y position goes down by 1
        // to move left down, x position goes down by 1 and y position goes down by 1
        // stops when it encounters a space occupied by its own or another one
        ArrayList<ChessMove> validMovesList = new ArrayList<ChessMove>();

        // do while loop for bishop up and right.
        ChessPiece newPosition;
        ChessPosition listPosition;
        ChessMove movedPiece;

        while((currentPosition.getRow() + x) <= 8 && (currentPosition.getColumn()+ x) <= 8){
            // make this the new position
            listPosition = new ChessPosition(currentPosition.getRow()+x,currentPosition.getColumn()+x);
            newPosition = board.getPiece(listPosition);

            if(newPosition == null){
                //add tuples to the list??
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);

            } else if (board.getPiece(currentPosition).getTeamColor() == newPosition.getTeamColor()) {
                break;
            }else{
                //captures other teams piece
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
                break;
            }

            x = x+1;
        }
        //do a while loop for bottom left diagonal
        x = 1;
        while((currentPosition.getRow() - x) > 0 && (currentPosition.getColumn()-x)>0){
            listPosition = new ChessPosition(currentPosition.getRow()-x,currentPosition.getColumn()-x);
            newPosition = board.getPiece(listPosition);
            if(newPosition == null){
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);

            } else if (board.getPiece(currentPosition).getTeamColor() == newPosition.getTeamColor()) {
                break;
            }else{
                //captures other teams piece
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
                break;
            }
            x = x+1;
        }

        x = 1;
        //do a while loop for top right diagonal
        while((currentPosition.getRow() + x) <= 8 && (currentPosition.getColumn()-x)>0){
            listPosition = new ChessPosition(currentPosition.getRow()+x,currentPosition.getColumn()-x);
            newPosition = board.getPiece(listPosition);
            if(newPosition == null){
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
            } else if (board.getPiece(currentPosition).getTeamColor() == newPosition.getTeamColor()) {
                break;
            }else{
                //captures other teams piece
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
                break;
            }
            x = x+1;

        }
        x = 1;
        //do a while loop for bottom right diagonal
        while((currentPosition.getRow() - x) > 0 && (currentPosition.getColumn() +x)<=8){
            listPosition = new ChessPosition(currentPosition.getRow()-x,currentPosition.getColumn()+x);
            newPosition = board.getPiece(listPosition);

            if(newPosition == null){
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
            } else if (board.getPiece(currentPosition).getTeamColor() == newPosition.getTeamColor()) {
                break;
            }else{
                //captures other team piece
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
                break;
            }
            x = x+1;

        }
        return validMovesList;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BishopMoves that = (BishopMoves) o;
        return x == that.x && Objects.equals(currentPosition, that.currentPosition) && Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, currentPosition, board);
    }

    @Override
    public String toString() {
        return "BishopMoves{" +
                "x=" + x +
                ", currentPosition=" + currentPosition +
                ", board=" + board +
                '}';
    }
}
