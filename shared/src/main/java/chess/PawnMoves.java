package chess;

import java.util.ArrayList;

public class PawnMoves implements PieceMovesCalculator{

    private ChessBoard board;
    private ChessPosition currentPosition;

    public PawnMoves (ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition = currentPosition;
    }

    public ArrayList<ChessMove> pawnMoves (int x, int y){
        ChessPiece ghostBishop;
        ChessPosition listedPosition;

        ChessMove movedPosition;
        ChessMove movedPosition2;
        ChessMove movedPosition3;
        ChessMove movedPosition4;

        ChessPiece currentPiece;

        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();
        currentPiece = board.getPiece(currentPosition);
        if(currentPosition.getRow()+x > 0 && currentPosition.getColumn()+y >0
                && currentPosition.getRow()+x <=8 & currentPosition.getColumn()+y <= 8) {
            listedPosition = new ChessPosition(currentPosition.getRow() + x, currentPosition.getColumn() + y);
            ghostBishop = board.getPiece(listedPosition);
            if (ghostBishop == null && (x==0 || y == 0)) {
                if(currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE && listedPosition.getRow() == 8){
                    moveAll(listedPosition, movedList);

                }else if(currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK && listedPosition.getRow() == 1){
                    moveAll(listedPosition, movedList);
                }
                else{
                    movedPosition = new ChessMove(currentPosition, listedPosition, null);
                    movedList.add(movedPosition);
                }

            }
            else if(ghostBishop != null){
                if (ghostBishop.getTeamColor() != board.getPiece(currentPosition).getTeamColor() && (y != 0)){
                    if(listedPosition.getRow() == 1){
                        moveAll(listedPosition, movedList);

                    }else if(listedPosition.getRow() == 8){
                        moveAll(listedPosition, movedList);

                    }else{
                        movedPosition = new ChessMove(currentPosition, listedPosition, null);
                        movedList.add(movedPosition);
                    }
                }else{return null;}
            }
            else {
                return null;
            }
        }
        return movedList;
    }

    private void moveAll(ChessPosition listedPosition, ArrayList<ChessMove> movedList) {
        ChessMove movedPosition2;
        ChessMove movedPosition4;
        ChessMove movedPosition3;
        ChessMove movedPosition;
        movedPosition = new ChessMove(currentPosition, listedPosition, ChessPiece.PieceType.KNIGHT);
        movedPosition2 = new ChessMove(currentPosition, listedPosition, ChessPiece.PieceType.BISHOP);
        movedPosition3 = new ChessMove(currentPosition, listedPosition, ChessPiece.PieceType.ROOK);
        movedPosition4 = new ChessMove(currentPosition, listedPosition, ChessPiece.PieceType.QUEEN);

        movedList.add(movedPosition);
        movedList.add(movedPosition2);
        movedList.add(movedPosition3);
        movedList.add(movedPosition4);
    }

    @Override
    public ArrayList<ChessMove> validMove() {
        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();
        if(board.getPiece(currentPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            if(pawnMoves(1,0)!= null){
                movedList.addAll(pawnMoves(1,0));
                if(pawnMoves(2,0)!= null && currentPosition.getRow() == 2){
                    movedList.addAll(pawnMoves(2,0));
                }
            }if(pawnMoves(1,1)!= null){
                movedList.addAll(pawnMoves(1,1));
            }if(pawnMoves(1,-1)!= null) {
                movedList.addAll(pawnMoves(1, -1));
            }
        }

        else if(board.getPiece(currentPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            if(pawnMoves(-1,0)!= null){
                movedList.addAll(pawnMoves(-1,0));
                if(pawnMoves(-2,0)!= null && currentPosition.getRow() == 7){
                    movedList.addAll(pawnMoves(-2,0));
                }
            }if(pawnMoves(-1,1)!= null){
                movedList.addAll(pawnMoves(-1,1));
            }if(pawnMoves(-1,-1)!= null) {
                movedList.addAll(pawnMoves(-1, -1));
            }
        }
        return movedList;
    }
}
