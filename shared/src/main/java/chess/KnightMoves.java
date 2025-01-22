package chess;

import java.util.ArrayList;
import java.util.Objects;

public class KnightMoves implements PieceMovesCalculator{

    private final ChessBoard board;
    private final ChessPosition currentPosition;
    private final int threeTile = 2;
    private final int oneTile = 1;

    public KnightMoves(ChessBoard board, ChessPosition currentPosition){
        this.board = board;
        this.currentPosition = currentPosition;
    }

    public ChessMove variedPosition(int row, int column) {
        ChessPiece ghostKnight;
        ChessPosition listedPosition;
        ChessMove movedPiece;
        if (((currentPosition.getRow() + row) <= 8 && (currentPosition.getRow() + row) > 0) && ((currentPosition.getColumn() + column) <= 8) && (currentPosition.getColumn() + column) > 0) {
            // make this the new position
            listedPosition = new ChessPosition(currentPosition.getRow() + row, currentPosition.getColumn() + column);
            ghostKnight = board.getPiece(listedPosition);

            if (ghostKnight == null || (ghostKnight.getTeamColor() != board.getPiece(currentPosition).getTeamColor())) {
                //add tuples to the list??
                movedPiece = new ChessMove(currentPosition, listedPosition, null);
                return movedPiece;

            }
        }
        return null; // I can only have this return statement as null if I write it in the valid moves
    }

    @Override
    public ArrayList<ChessMove> validMove() {

        ArrayList<ChessMove> validMovesList = new ArrayList<ChessMove>();


        if(variedPosition(-threeTile,-oneTile) != null){
            validMovesList.add(variedPosition(-threeTile,-oneTile));
        }
        if(variedPosition(-threeTile,oneTile) != null){
            validMovesList.add(variedPosition(-threeTile,oneTile));
        }
        if(variedPosition(threeTile,-oneTile) != null){
            validMovesList.add(variedPosition(threeTile,-oneTile));
        }
        if(variedPosition(threeTile,oneTile) != null){
            validMovesList.add(variedPosition(threeTile,oneTile));
        }
        if(variedPosition(-oneTile,-threeTile) != null){
            validMovesList.add(variedPosition(-oneTile,-threeTile));
        }
        if(variedPosition(-oneTile,threeTile) != null){
            validMovesList.add(variedPosition(-oneTile,threeTile));
        }
        if(variedPosition(oneTile,-threeTile) != null){
            validMovesList.add(variedPosition(oneTile,-threeTile));
        }
        if(variedPosition(oneTile,threeTile) != null){
            validMovesList.add(variedPosition(oneTile,threeTile));
        }

        return validMovesList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KnightMoves that = (KnightMoves) o;
        return threeTile == that.threeTile && oneTile == that.oneTile && Objects.equals(board, that.board) && Objects.equals(currentPosition, that.currentPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentPosition, threeTile, oneTile);
    }

    @Override
    public String toString() {
        return "KnightMoves{" +
                "board=" + board +
                ", currentPosition=" + currentPosition +
                ", threeTile=" + threeTile +
                ", oneTile=" + oneTile +
                '}';
    }
}

