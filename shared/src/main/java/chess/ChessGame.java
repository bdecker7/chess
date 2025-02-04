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

    //If the king is not in check on this team, get valid moves
    //If the king is not left in danger from moving this piece, return moves
    //if piece is not null

//    Takes as input a position on the chessboard and returns all moves the piece there can legally make.
//    If there is no piece at that location, this method returns null. A move is valid if it is a "piece move" for
//    the piece at the input location and making that move would not leave the team’s king in danger of check.

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> pieceMoves = new ArrayList<ChessMove>();
        if(gameBoard.getPiece(startPosition) != null ) { //checks current position to see if it is valid
            if (!isInCheck(gameBoard.getPiece(startPosition).getTeamColor())) {
                pieceMoves.addAll(gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition)); //checks if king not in check
                return pieceMoves;
            }

        } else{return null;}
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    //A move is illegal if it is not a "valid" move for the piece at the starting location,
    // or if it’s not the corresponding team's turn.

//    Receives a given move and executes it, provided it is a legal move. If the move is illegal, it throws an InvalidMoveException.
//    A move is illegal if it is not a "valid" move for the piece at the starting location, or if it’s not the corresponding team's turn.
    public void makeMove(ChessMove move) throws InvalidMoveException {

//        ArrayList<ChessMove> validMove = new ArrayList<>();
//        validMove = validMoves(move.getStartPosition());
        if(validMoves(move.getStartPosition()) == null || !(gameBoard.getPiece(move.getStartPosition()).getTeamColor() == turn)){
            throw new InvalidMoveException("Invalid move!");}

        else if(validMoves(move.getStartPosition()) != null){
         //   ChessPiece currentPiece = new ChessPiece(gameBoard.getPiece(move.getStartPosition()).getTeamColor(),gameBoard.getPiece(move.getStartPosition()).getPieceType());
            if(validMoves(move.getStartPosition()).contains(move)){

                if(move.getPromotionPiece() != null){
                    ChessPiece currentPiece = new ChessPiece(gameBoard.getPiece(move.getStartPosition()).getTeamColor(),move.getPromotionPiece());
                    gameBoard.addPiece(move.getEndPosition(), currentPiece); //adds piece to desired end position
                    gameBoard.addPiece(move.getStartPosition(), null);
                }else {
                    gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition())); //adds piece to desired end position
                    gameBoard.addPiece(move.getStartPosition(), null); // sets previous position to null piece
                }

                // Change turns when move is executed
                if(gameBoard.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.WHITE)
                    setTeamTurn(TeamColor.BLACK);
                else{setTeamTurn(TeamColor.WHITE);}
            }else{throw new InvalidMoveException("Invalid move!");}

        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    //Returns true if the specified team’s King could be captured by an opposing piece.

    public boolean isInCheck(TeamColor teamColor) {
        //iterate through all the opposite colored pieces.
        // if the piece contains the same position as the king of current team color, return true
        Collection<ChessMove> OtherTeamMoves = new ArrayList<ChessMove>();
        ChessPosition currentTeamsKingPosition = null;
        ChessMove teamsKingMoves = null;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentGamePiece = gameBoard.getPiece(currentPosition);

                //grabs all the moves of the opposing side, and the position of the current kings team
                if(currentGamePiece != null){
                    if(currentGamePiece.getTeamColor() != teamColor){ //if it's not the same team, get collection of moves it can take
                        OtherTeamMoves.addAll(currentGamePiece.pieceMoves(gameBoard,currentPosition));
                    }
                    else if(currentGamePiece.getPieceType() == ChessPiece.PieceType.KING && (currentGamePiece.getTeamColor() == teamColor)){
                        currentTeamsKingPosition = new ChessPosition(i,j);
                    }
                }

            }
        }
        for(ChessMove element: OtherTeamMoves){
            if(element.getEndPosition().getRow() == currentTeamsKingPosition.getRow() && element.getEndPosition().getColumn() == currentTeamsKingPosition.getColumn()){ //this is not returning true when it's supposed to
                return true;
            }
        }
        return false;//the currentTeamsKingPosition is a position so the contains doesnt work here.
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
