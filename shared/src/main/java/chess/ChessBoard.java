package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;

    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.deepToString(squares) +
                '}';
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //deletes everything on the board by setting it to null
        // adds pieces to the board directly depending on i and j positions
        for(int i = 0 ; i < 8; i++ ){
            for(int j = 0; j < 8; j++){
                squares[i][j] = null;   // deletes everything on the board
                ChessPosition position = new ChessPosition(i+1,j+1);
                if(i == 1){                              //White pawns
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
                    squares[i][j] = piece;
                }else if(i == 6){                       // Black Pawns placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
                    squares[i][j] = piece;
                }else if(i == 0 && (j == 0 || j == 7)){ //White Rook placement      //This is where it fails
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK);
                    squares[i][j] = piece;   //This is where it fails. It seems like the position is off on the function here
                }else if(i == 7 && (j == 0 || j == 7)){ //Black Rook placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK);
                    squares[i][j] = piece;
                }else if(i == 0 && (j == 1 || j == 6)){ //White Knight placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT);
                    squares[i][j] = piece;
                }else if(i == 7 && (j == 1 || j == 6)){ //Black Knight placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT);
                    squares[i][j] = piece;
                }else if(i == 0 && (j == 2 || j == 5)){ //White Bishop placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP);
                    squares[i][j] = piece;
                }else if(i == 7 && (j == 2 || j == 5)){ //Black Bishop placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP);
                    squares[i][j] = piece;
                }else if(i == 0 && (j == 3)){ //White KING placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.QUEEN);
                    squares[i][j] = piece;
                }else if(i == 0 && (j == 4)){ //White QUEEN placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KING);
                    squares[i][j] = piece;
                }else if(i == 7 && (j == 3)){ //Black KING placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.QUEEN);
                    squares[i][j] = piece;
                }else if(i == 7 && (j == 4)){ //Black QUEEN placement
                    ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KING);
                    squares[i][j] = piece;
                }


            }
        }

    }
}
