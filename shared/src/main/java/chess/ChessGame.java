package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard gameBoard = new ChessBoard();
    TeamColor turn = TeamColor.WHITE;
    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;

    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(gameBoard.getPiece(startPosition) != null ){
            //If statement for if the king is left in check from this team from this move
            if(!isInCheck(gameBoard.getPiece(startPosition).getTeamColor())){
                return gameBoard.getPiece(startPosition).pieceMoves(gameBoard,startPosition);}
        }else{return null;}
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(validMoves(move.getStartPosition()) != null){
            if(validMoves(move.getEndPosition()) != null){
                if(!validMoves(move.getEndPosition()).contains(move)) {
                    throw new InvalidMoveException("Invalid move!");
                }
            }
            else if(!validMoves(move.getStartPosition()).contains(move)){
                throw new InvalidMoveException("Invalid move!") ;
            }
            else{
                gameBoard.addPiece(move.getEndPosition(),gameBoard.getPiece(move.getStartPosition())); //adds piece to desired end position
                gameBoard.addPiece(move.getStartPosition(),null); // sets previous position to null piece
            }
        }else{throw new InvalidMoveException("Invalid move!");} // not sure about this line of code
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    

    public boolean isInCheck(TeamColor teamColor) {
        //iterate through all the opposite colored pieces.
        // if the piece contains the same position as the king of current team color, return true
        Collection<ChessMove> OtherTeamMoves = new ArrayList<ChessMove>();
        ChessPosition kingPosition = null;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j<= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentGamePiece = gameBoard.getPiece(currentPosition);
                if(currentGamePiece != null){
                    if(currentGamePiece.getTeamColor() != teamColor){ //if it's not the same team, get collection of moves it can take
                        OtherTeamMoves.addAll(currentGamePiece.pieceMoves(gameBoard,currentPosition));
                    }
                    else if(currentGamePiece.getPieceType() == ChessPiece.PieceType.KING && currentGamePiece.getTeamColor() == teamColor){
                        kingPosition = new ChessPosition(i,j);
                    }
                }
            }
        }
        return OtherTeamMoves.contains(kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
