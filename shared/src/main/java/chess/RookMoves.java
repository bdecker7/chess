package chess;

import java.util.ArrayList;

public class RookMoves implements PieceMovesCalculator{
    ChessBoard board;
    ChessPosition currentPosition;

    public RookMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition= currentPosition;
    }

    public ArrayList<ChessMove> upDownMoves(int x){
        ChessPiece ghostBishop;
        ChessPosition listedPosition;
        ChessMove movedPosition;

        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();

        while(currentPosition.getRow()+x <= 8 && currentPosition.getRow()+x > 0){
            listedPosition = new ChessPosition(currentPosition.getRow()+x,currentPosition.getColumn());
            ghostBishop = board.getPiece(listedPosition);
            if (moves(ghostBishop, listedPosition, movedList)) {
                break;
            }


            if (x>0){
                x += 1;
            }else{
                x = x-1;
            }
        }

        return movedList;
    }

    private boolean moves(ChessPiece ghostBishop, ChessPosition listedPosition, ArrayList<ChessMove> movedList) {
        ChessMove movedPosition;
        if(ghostBishop == null){
            movedPosition = new ChessMove(currentPosition, listedPosition,null);
            movedList.add(movedPosition);
        }else if(ghostBishop.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
            movedPosition = new ChessMove(currentPosition, listedPosition,null);
            movedList.add(movedPosition);
            return true;
        }else{
            return true;
        }
        return false;
    }

    public ArrayList<ChessMove> sideMoves(int x){
        ChessPiece ghostBishop;
        ChessPosition listedPosition;
        ChessMove movedPosition;

        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();

        while(currentPosition.getColumn()+x <= 8 && currentPosition.getColumn()+x > 0){
            listedPosition = new ChessPosition(currentPosition.getRow(),currentPosition.getColumn()+x);
            ghostBishop = board.getPiece(listedPosition);
            if (moves(ghostBishop, listedPosition, movedList)) {
                break;
            }


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

        if(upDownMoves(1) != null) {
            movedListFinal.addAll(upDownMoves(1));
        }if(upDownMoves(-1) != null) {
            movedListFinal.addAll(upDownMoves(-1));
        }if(sideMoves(1) != null) {
            movedListFinal.addAll(sideMoves(1));
        }if(sideMoves(-1) != null) {
            movedListFinal.addAll(sideMoves(-1));
        }
        return movedListFinal;
    }

}
