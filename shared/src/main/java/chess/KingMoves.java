package chess;

import java.util.ArrayList;

public class KingMoves implements PieceMovesCalculator{
    private ChessBoard board;
    private ChessPosition currentPosition;
    private int oneTileMove = 1;

    public KingMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition = currentPosition;
    }

    private ChessMove moveOneTile(int x, int y){
        ChessPiece ghostKing;
        ChessMove movedPiece;
        ChessPosition listedPosition;

        if(((currentPosition.getRow()+x) > 0 && (currentPosition.getRow()+x) <= 8) && (((currentPosition.getColumn()+y)> 0) && ((currentPosition.getColumn()+y)<=8))){
            listedPosition = new ChessPosition(currentPosition.getRow()+x,currentPosition.getColumn()+y);
            ghostKing = board.getPiece(listedPosition);
            if(ghostKing == null || ghostKing.getTeamColor() != board.getPiece(currentPosition).getTeamColor()){
                movedPiece = new ChessMove(currentPosition,listedPosition,null);
                return movedPiece;
            }
        }return null;
    }

    @Override
    public ArrayList<ChessMove> validMove() {

        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();

        //moves in diagonal directions
        if(moveOneTile(oneTileMove,oneTileMove) != null) {
            moveList.add(moveOneTile(oneTileMove,oneTileMove));
        }if(moveOneTile(oneTileMove,-oneTileMove) != null) {
            moveList.add(moveOneTile(oneTileMove,-oneTileMove));
        }if(moveOneTile(-oneTileMove,oneTileMove) != null) {
            moveList.add(moveOneTile(-oneTileMove,oneTileMove));
        }if(moveOneTile(-oneTileMove,-oneTileMove) != null) {
            moveList.add(moveOneTile(-oneTileMove,-oneTileMove));

            //moves up, down, left, right
        }if(moveOneTile(oneTileMove,0) != null) {
            moveList.add(moveOneTile(oneTileMove,0));
        }if(moveOneTile(-oneTileMove,0) != null) {
            moveList.add(moveOneTile(-oneTileMove,0));
        }if(moveOneTile(0,-oneTileMove) != null) {
            moveList.add(moveOneTile(0,-oneTileMove));
        }if(moveOneTile(0,oneTileMove) != null) {
            moveList.add(moveOneTile(0,oneTileMove));
        }
        return moveList;
    }
}
