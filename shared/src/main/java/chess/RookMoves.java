//package chess;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class RookMoves implements PieceMovesCalculator{
//
//    private final ChessBoard board;
//    private final ChessPosition currentPosition;
//    private int x = 1;
//
//    public RookMoves(ChessBoard board, ChessPosition currentPosition){
//        this.board = board;
//        this.currentPosition = currentPosition;
//    }
//    @Override
//    public ArrayList<ChessMove> validMove() {
//        ArrayList<ChessMove> validMovesList = new ArrayList<ChessMove>();
//
//        ChessPiece ghostPiece;
//        ChessPosition listPosition;
//        ChessMove movedPiece;
//
//        //left row
//        while(currentPosition.getRow()+x <= 8){
//            listPosition = new ChessPosition(currentPosition.getRow()+x, currentPosition.getColumn() );
//            ghostPiece = board.getPiece(listPosition);
//            if(ghostPiece == null){
//                movedPiece = new ChessMove(currentPosition,listPosition,null);
//                validMovesList.add(movedPiece);
//            }else if(ghostPiece.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
//                break;
//            }else{
//                movedPiece = new ChessMove(currentPosition,listPosition,null);
//                validMovesList.add(movedPiece);
//                break;
//            }
//            x+=1;
//        }
//        x = 1;
//        while(currentPosition.getRow()-x > 0){
//            listPosition = new ChessPosition(currentPosition.getRow()-x, currentPosition.getColumn() );
//            ghostPiece = board.getPiece(listPosition);
//            if(ghostPiece == null){
//                movedPiece = new ChessMove(currentPosition,listPosition,null);
//                validMovesList.add(movedPiece);
//            }else if(ghostPiece.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
//                break;
//            }else{
//                movedPiece = new ChessMove(currentPosition,listPosition,null);
//                validMovesList.add(movedPiece);
//                break;
//            }
//            x+=1;
//        }
//        x = 1;
//        while(currentPosition.getColumn()-x > 0){
//            listPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn()-x );
//            ghostPiece = board.getPiece(listPosition);
//            if(ghostPiece == null){
//                movedPiece = new ChessMove(currentPosition,listPosition,null);
//                validMovesList.add(movedPiece);
//            }else if(ghostPiece.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
//                break;
//            }else{
//                movedPiece = new ChessMove(currentPosition,listPosition,null);
//                validMovesList.add(movedPiece);
//                break;
//            }
//            x+=1;
//        }
//        x=1;
//        while(currentPosition.getColumn()+x <= 8){
//            listPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn()+x );
//            ghostPiece = board.getPiece(listPosition);
//            if(ghostPiece == null){
//                movedPiece = new ChessMove(currentPosition,listPosition,null);
//                validMovesList.add(movedPiece);
//            }else if(ghostPiece.getTeamColor() == board.getPiece(currentPosition).getTeamColor()){
//                break;
//            }else{
//                movedPiece = new ChessMove(currentPosition,listPosition,null);
//                validMovesList.add(movedPiece);
//                break;
//            }
//            x+=1;
//        }
//
//        return validMovesList;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        RookMoves rookMoves = (RookMoves) o;
//        return x == rookMoves.x && Objects.equals(board, rookMoves.board) && Objects.equals(currentPosition, rookMoves.currentPosition);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(board, currentPosition, x);
//    }
//
//    @Override
//    public String toString() {
//        return "RookMoves{" +
//                "board=" + board +
//                ", currentPosition=" + currentPosition +
//                ", x=" + x +
//                '}';
//    }
//}
package chess;

import java.util.ArrayList;

public class RookMoves implements PieceMovesCalculator{
    ChessBoard board;
    ChessPosition currentPosition;

    public RookMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition= currentPosition;
    }

    public ArrayList<ChessMove> UpDownMoves(int x){
        ChessPiece ghostBishop;
        ChessPosition listedPosition;
        ChessMove movedPosition;

        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();

        while(currentPosition.getRow()+x <= 8 && currentPosition.getRow()+x > 0){
            listedPosition = new ChessPosition(currentPosition.getRow()+x,currentPosition.getColumn());
            ghostBishop = board.getPiece(listedPosition);
            if(ghostBishop == null){
                movedPosition = new ChessMove(currentPosition,listedPosition,null);
                movedList.add(movedPosition);
            }else if(ghostBishop.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                movedPosition = new ChessMove(currentPosition,listedPosition,null);
                movedList.add(movedPosition);
                break;
            }else{break;}


            if (x>0){
                x += 1;
            }else{
                x = x-1;
            }
        }

        return movedList;
    }
    public ArrayList<ChessMove> SideMoves(int x){
        ChessPiece ghostBishop;
        ChessPosition listedPosition;
        ChessMove movedPosition;

        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();

        while(currentPosition.getColumn()+x <= 8 && currentPosition.getColumn()+x > 0){
            listedPosition = new ChessPosition(currentPosition.getRow(),currentPosition.getColumn()+x);
            ghostBishop = board.getPiece(listedPosition);
            if(ghostBishop == null){
                movedPosition = new ChessMove(currentPosition,listedPosition,null);
                movedList.add(movedPosition);
            }else if(ghostBishop.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                movedPosition = new ChessMove(currentPosition,listedPosition,null);
                movedList.add(movedPosition);
                break;
            }else{break;}


            if (x>0){
                x += 1;
            }else{
                x = x-1;
            }
        }

        return movedList;
    }


    @Override
    public ArrayList<ChessMove> validMove() {
        ArrayList<ChessMove> movedListFinal = new ArrayList<ChessMove>();

        if(UpDownMoves(1) != null) {
            movedListFinal.addAll(UpDownMoves(1));
        }if(UpDownMoves(-1) != null) {
            movedListFinal.addAll(UpDownMoves(-1));
        }if(SideMoves(1) != null) {
            movedListFinal.addAll(SideMoves(1));
        }if(SideMoves(-1) != null) {
            movedListFinal.addAll(SideMoves(-1));
        }
        return movedListFinal;
    }

}
