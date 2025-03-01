package chess;

import java.util.ArrayList;

public class KnightMoves implements PieceMovesCalculator{
    private ChessBoard board;
    private ChessPosition currentPosition;
    public KnightMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition = currentPosition;
    }
    public ArrayList<ChessMove> horseMoves (int x, int y){
        ChessPiece ghostBishop;
        ChessPosition listedPosition;
        ChessMove movedPosition;

        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();

        if(currentPosition.getRow()+x > 0 && currentPosition.getColumn()+y >0 && currentPosition.getRow()+x <=8 & currentPosition.getColumn()+y <= 8) {
            listedPosition = new ChessPosition(currentPosition.getRow() + x, currentPosition.getColumn() + y);
            ghostBishop = board.getPiece(listedPosition);
            if (ghostBishop == null) {
                movedPosition = new ChessMove(currentPosition, listedPosition, null);
                movedList.add(movedPosition);
            } else if (ghostBishop.getTeamColor() != board.getPiece(currentPosition).getTeamColor()) {
                movedPosition = new ChessMove(currentPosition, listedPosition, null);
                movedList.add(movedPosition);
            } else {
                return null;
            }
        }
        return movedList;
    }

    @Override
    public ArrayList<ChessMove> validMove() {
        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();
        if(horseMoves(1,2)!= null){
            movedList.addAll(horseMoves(1,2));
        }if(horseMoves(-1,2)!= null){
            movedList.addAll(horseMoves(-1,2));
        }if(horseMoves(1,-2)!= null){
            movedList.addAll(horseMoves(1,-2));
        }if(horseMoves(-1,-2)!= null){
            movedList.addAll(horseMoves(-1,-2));
        }

        if(horseMoves(2,1)!= null){
            movedList.addAll(horseMoves(2,1));
        }if(horseMoves(-2,1)!= null){
            movedList.addAll(horseMoves(-2,1));
        }if(horseMoves(2,-1)!= null){
            movedList.addAll(horseMoves(2,-1));
        }if(horseMoves(-2,-1)!= null){
            movedList.addAll(horseMoves(-2,-1));
        }

        return movedList;
    }
}
