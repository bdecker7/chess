package chess;

import java.util.ArrayList;
import java.util.Objects;

public class RookMoves implements PieceMovesCalculator{

    private final ChessBoard board;
    private final ChessPosition currentPosition;
    private int x = 1;

    public RookMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition = currentPosition;
    }
    @Override
    public ArrayList<ChessMove> validMove() {
        ArrayList<ChessMove> validMovesList = new ArrayList<ChessMove>();

        ChessPiece ghostPiece;
        ChessPosition listPosition;
        ChessMove movedPiece;

        //left row
        while(currentPosition.getRow()+x <= 8){
            listPosition = new ChessPosition(currentPosition.getRow()+x, currentPosition.getColumn() );
            ghostPiece = board.getPiece(listPosition);
            if(ghostPiece == null){
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
            }else if(ghostPiece.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
                break;
            }else{
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
                break;
            }
            x+=1;
        }
        x = 1;
        while(currentPosition.getRow()-x > 0){
            listPosition = new ChessPosition(currentPosition.getRow()-x, currentPosition.getColumn() );
            ghostPiece = board.getPiece(listPosition);
            if(ghostPiece == null){
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
            }else if(ghostPiece.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
                break;
            }else{
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
                break;
            }
            x+=1;
        }
        x = 1;
        while(currentPosition.getColumn()-x > 0){
            listPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn()-x );
            ghostPiece = board.getPiece(listPosition);
            if(ghostPiece == null){
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
            }else if(ghostPiece.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
                break;
            }else{
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
                break;
            }
            x+=1;
        }
        x=1;
        while(currentPosition.getColumn()+x <= 8){
            listPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn()+x );
            ghostPiece = board.getPiece(listPosition);
            if(ghostPiece == null){
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
            }else if(ghostPiece.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
                break;
            }else{
                movedPiece = new ChessMove(currentPosition,listPosition,null);
                validMovesList.add(movedPiece);
                break;
            }
            x+=1;
        }

        return validMovesList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RookMoves rookMoves = (RookMoves) o;
        return x == rookMoves.x && Objects.equals(board, rookMoves.board) && Objects.equals(currentPosition, rookMoves.currentPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentPosition, x);
    }

    @Override
    public String toString() {
        return "RookMoves{" +
                "board=" + board +
                ", currentPosition=" + currentPosition +
                ", x=" + x +
                '}';
    }
}
