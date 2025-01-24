package chess;

import java.util.ArrayList;
import java.util.Objects;

public class PawnMoves implements PieceMovesCalculator{

    private ChessBoard board;
    private ChessPosition currentPosition;
    private ChessGame.TeamColor Color;
    private int upOne = 1;
    private int overOne = 1;
    private ChessPosition listedPosition;
    private ChessPiece ghostPawn;
    private ChessMove movedPosition;


    public PawnMoves(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor Color){
        this.board = board;
        this.currentPosition = currentPosition;
        this.Color = Color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PawnMoves pawnMoves = (PawnMoves) o;
        return upOne == pawnMoves.upOne && overOne == pawnMoves.overOne && Objects.equals(board, pawnMoves.board) && Objects.equals(currentPosition, pawnMoves.currentPosition) && Color == pawnMoves.Color && Objects.equals(listedPosition, pawnMoves.listedPosition) && Objects.equals(ghostPawn, pawnMoves.ghostPawn) && Objects.equals(movedPosition, pawnMoves.movedPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentPosition, Color, upOne, overOne, listedPosition, ghostPawn, movedPosition);
    }

    @Override
    public String toString() {
        return "PawnMoves{" +
                "board=" + board +
                ", currentPosition=" + currentPosition +
                ", Color=" + Color +
                ", upOne=" + upOne +
                ", overOne=" + overOne +
                ", listedPosition=" + listedPosition +
                ", ghostPawn=" + ghostPawn +
                ", movedPosition=" + movedPosition +
                '}';
    }

    //Check if anything in front immediately and in bounds- returns either null or the single move
    // ---- Checks if it has reached the end
    public ArrayList<ChessMove> moveUpOne(int x){
        ChessMove movedPosition2;
        ChessMove movedPosition3;
        ChessMove movedPosition4;
        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();

        listedPosition = new ChessPosition(currentPosition.getRow()+x, currentPosition.getColumn());
        ghostPawn = board.getPiece(listedPosition);
        if(ghostPawn == null && listedPosition.getRow() <= 8 && (listedPosition.getRow() > 0)){
            if(listedPosition.getRow() == 8 || listedPosition.getRow() == 1){
                //Promote piece -- how to do that??
                movedPosition = new ChessMove(currentPosition,listedPosition, ChessPiece.PieceType.QUEEN);
                movedPosition2 = new ChessMove(currentPosition,listedPosition, ChessPiece.PieceType.ROOK);
                movedPosition3 = new ChessMove(currentPosition,listedPosition, ChessPiece.PieceType.KNIGHT);
                movedPosition4 = new ChessMove(currentPosition,listedPosition, ChessPiece.PieceType.BISHOP);

                movedList.add(movedPosition);
                movedList.add(movedPosition2);
                movedList.add(movedPosition3);
                movedList.add(movedPosition4);
                return movedList;
            }else{
                movedPosition = new ChessMove(currentPosition,listedPosition,null);
                movedList.add(movedPosition);
                return movedList;
            }
        }
        return null;
    }


    //Check if it is clear diagonally and in bounds
    public ArrayList<ChessMove> diagonalMoves(int x, int y){
        ChessMove movedPosition2;
        ChessMove movedPosition3;
        ChessMove movedPosition4;
        ArrayList<ChessMove> movedList= new ArrayList<ChessMove>();

        //check to see if in bounds
        listedPosition = new ChessPosition(currentPosition.getRow() + x, currentPosition.getColumn() + y);
        if((listedPosition.getRow()) <= 8 && (listedPosition.getColumn() <= 8)&& (listedPosition.getRow()) > 0 && (listedPosition.getColumn() > 0)) {
            ghostPawn = board.getPiece(listedPosition);
        }

        if(ghostPawn != null){
            if(listedPosition.getRow() == 8 || listedPosition.getRow() == 1){
                //Promote piece -- how to do that??
                //make 4 with each type

                movedPosition = new ChessMove(currentPosition,listedPosition, ChessPiece.PieceType.QUEEN);
                movedPosition2 = new ChessMove(currentPosition,listedPosition, ChessPiece.PieceType.ROOK);
                movedPosition3 = new ChessMove(currentPosition,listedPosition, ChessPiece.PieceType.KNIGHT);
                movedPosition4 = new ChessMove(currentPosition,listedPosition, ChessPiece.PieceType.BISHOP);

                movedList.add(movedPosition);
                movedList.add(movedPosition2);
                movedList.add(movedPosition3);
                movedList.add(movedPosition4);
                return movedList;

            }else if(ghostPawn.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                movedPosition = new ChessMove(currentPosition,listedPosition,null);
                movedList.add(movedPosition);
                return movedList;
            }
        }
        return null;
    }


    //Check if first move and second square open-- returns either null or the double move
    public ChessMove firstMove(int x){

        //first move was checked already. now just
        return null;
    }



    @Override
    public ArrayList<ChessMove> validMove() {

        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();
        if (Color == ChessGame.TeamColor.WHITE){
            //check if first move -- then call the first move function
            //regardless, see if calling oneMove method doesnt give us null;
            if(moveUpOne(upOne) != null){
                movedList.addAll(moveUpOne(upOne));
                if(currentPosition.getRow() == 2 && moveUpOne(upOne+upOne)!= null){
                    movedList.addAll(moveUpOne(upOne+upOne));
                }
            }if(diagonalMoves(upOne,overOne) != null){
                movedList.addAll(diagonalMoves(upOne,overOne));
            }if(diagonalMoves(upOne,-overOne) != null){
                movedList.addAll(diagonalMoves(upOne,-overOne));
            }

            return movedList;
        }

        if(Color == ChessGame.TeamColor.BLACK){
            //these have negative "x" values because they move from top to bottom.
            if(moveUpOne(-upOne) != null){
                movedList.addAll(moveUpOne(-upOne));
                if(currentPosition.getRow()==7 && moveUpOne(-upOne-upOne) != null){
                    movedList.addAll(moveUpOne(-upOne-upOne));
                }
            }if(diagonalMoves(-upOne,overOne) != null){
                movedList.addAll(diagonalMoves(-upOne,overOne));
            }if(diagonalMoves(-upOne,-overOne) != null){
                movedList.addAll(diagonalMoves(-upOne,-overOne));
            }
            return movedList;
        }

        return movedList;
    }
}
