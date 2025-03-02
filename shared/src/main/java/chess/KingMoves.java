package chess;

import java.util.ArrayList;

public class KingMoves implements PieceMovesCalculator{
    ChessBoard board;
    ChessPosition currentPosition;

    public KingMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition= currentPosition;
    }
    public ArrayList<ChessMove> helperMoves(int x, int y) {
        ChessPiece ghostKing;
        ChessPosition listedPosition;
        ChessMove movedPosition;

        ArrayList<ChessMove> movedList = new ArrayList<ChessMove>();

        if (currentPosition.getRow() + x <= 8 && currentPosition.getColumn() + y > 0
                && currentPosition.getRow() + x > 0 && currentPosition.getColumn() + y <= 8) {
            listedPosition = new ChessPosition(currentPosition.getRow() + x, currentPosition.getColumn() + y);
            ghostKing = board.getPiece(listedPosition);
            if (ghostKing == null) {
                movedPosition = new ChessMove(currentPosition, listedPosition, null);
                movedList.add(movedPosition);
            } else if (ghostKing.getTeamColor() != board.getPiece(currentPosition).getTeamColor()) {
                movedPosition = new ChessMove(currentPosition, listedPosition, null);
                movedList.add(movedPosition);
            }
        }
        return movedList;
    }
    @Override
    public ArrayList<ChessMove> validMove() {
        ArrayList<ChessMove> movedListFinal = new ArrayList<ChessMove>();

        if (helperMoves(1, 0) != null) {
            movedListFinal.addAll(helperMoves(1, 0));

        }if (helperMoves(-1, 0) != null) {
            movedListFinal.addAll(helperMoves(-1, 0));
        }if (helperMoves(0, 1) != null) {
            movedListFinal.addAll(helperMoves(0, 1));
        }if (helperMoves(0, 1) != null) {
            movedListFinal.addAll(helperMoves(0, -1));
        }

        if (helperMoves(1, 1) != null) {
            movedListFinal.addAll(helperMoves(1, 1));
        }
        if (helperMoves(-1, 1) != null) {
            movedListFinal.addAll(helperMoves(-1, 1));
        }if (helperMoves(-1, -1) != null) {
            movedListFinal.addAll(helperMoves(-1, -1));
        }if (helperMoves(1, -1) != null) {
            movedListFinal.addAll(helperMoves(1, -1));
        }

        return movedListFinal;
    }
}