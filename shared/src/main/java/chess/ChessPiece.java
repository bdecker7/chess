package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> movesList = new ArrayList<ChessMove>();
        ArrayList<ChessMove> movesList2 = new ArrayList<ChessMove>();

        ChessPiece selectedPiece = board.getPiece(myPosition);

        if(selectedPiece.getPieceType() == PieceType.PAWN && selectedPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            PawnMoves pawn = new PawnMoves(board, myPosition, ChessGame.TeamColor.WHITE);
            return pawn.validMove();
        }else if(selectedPiece.getPieceType() == PieceType.PAWN && selectedPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
            PawnMoves pawn = new PawnMoves(board, myPosition, ChessGame.TeamColor.BLACK);
            return pawn.validMove();
        }else if (selectedPiece.getPieceType() == PieceType.BISHOP) {
            BishopMoves bishop = new BishopMoves(board,myPosition);
            return bishop.validMove();
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            KnightMoves knight = new KnightMoves(board, myPosition);
            return knight.validMove();
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            RookMoves rook = new RookMoves(board,myPosition);
            return rook.validMove();
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            KingMoves king = new KingMoves(board,myPosition);
            return king.validMove();
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            //Queen moves just use Bishop moves and Rook moves so this gets both and combines them in a list
            BishopMoves queen1 = new BishopMoves(board,myPosition);
            RookMoves queen2 = new RookMoves(board,myPosition);
            movesList = queen1.validMove();
            movesList2 = queen2.validMove();
            movesList.addAll(movesList2);
            return movesList;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
