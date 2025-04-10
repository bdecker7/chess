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
    public Boolean isResigned = false;
    public Boolean isCheckmate = false;
    public Boolean isStalemate = false;

    public ChessGame() {
        gameBoard.resetBoard();
    }

    public Boolean getIsResigned(){
        return isResigned;
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

//    Takes as input a position on the chessboard and returns all moves the piece there can legally make.
//    If there is no piece at that location, this method returns null. A move is valid if it is a "piece move" for
//    the piece at the input location and making that move would not leave the team’s king in danger of check.

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        if(gameBoard.getPiece(startPosition) != null ) { //checks current position to see if it is valid
            ArrayList<ChessMove> pieceMoves =
                    new ArrayList<ChessMove>(gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition));
            ArrayList<ChessMove> finalMoves = new ArrayList<ChessMove>();
            //Check to see if you move the piece then king will be in check.
            if (!isInCheck(gameBoard.getPiece(startPosition).getTeamColor())) {
                //checks if king not in check
                for(ChessMove element: pieceMoves) {
                    doMove(startPosition, element, finalMoves);
                }
            }else{
                //try out the area and see if it is in check. if in check, delete that from the list of moves
                for(ChessMove element: pieceMoves){
                    doMove(startPosition, element, finalMoves);
                }
            }return finalMoves;

        } else{return null;}
    }

    private void doMove(ChessPosition startPosition, ChessMove element, ArrayList<ChessMove> finalMoves) {
        if (gameBoard.getPiece(element.getEndPosition()) != null
                && gameBoard.getPiece(element.getEndPosition()).getTeamColor()
                != gameBoard.getPiece(startPosition).getTeamColor()) {
            ChessPiece ghostPiece =
                    new ChessPiece(gameBoard.getPiece(element.getEndPosition()).getTeamColor(),
                    gameBoard.getPiece(element.getEndPosition()).getPieceType());
            gameBoard.addPiece(element.getEndPosition(), gameBoard.getPiece(element.getStartPosition()));
            gameBoard.addPiece(element.getStartPosition(), null);
            if (!isInCheck(gameBoard.getPiece(element.getEndPosition()).getTeamColor())) {
                finalMoves.add(element);
            }
            gameBoard.addPiece(element.getStartPosition(), gameBoard.getPiece(element.getEndPosition()));
            gameBoard.addPiece(element.getEndPosition(), ghostPiece);
        } else if (gameBoard.getPiece(element.getEndPosition()) == null) {
            gameBoard.addPiece(element.getEndPosition(), gameBoard.getPiece(element.getStartPosition()));
            gameBoard.addPiece(element.getStartPosition(), null);
            if (!isInCheck(gameBoard.getPiece(element.getEndPosition()).getTeamColor())) {
                finalMoves.add(element);
            }
            gameBoard.addPiece(element.getStartPosition(), gameBoard.getPiece(element.getEndPosition()));
            gameBoard.addPiece(element.getEndPosition(), null);
        }
    }


    public void changeResignedStatus(boolean choice){
        isResigned = choice;
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */

    public void makeMove(ChessMove move) throws InvalidMoveException {

        if(isResigned){
            throw new GameOverException("Can't make move, game is over.");
        }else if(isInCheckmate(TeamColor.BLACK) || isInCheckmate(TeamColor.WHITE)){
            throw new GameOverException("Checkmate! Thanks for playing!");
        }else if(isInStalemate(TeamColor.BLACK)|| isInStalemate(TeamColor.WHITE)){
            throw new GameOverException("Stalemate! Can't make any more moves");
        }

        if(validMoves(move.getStartPosition()) == null
                || !(gameBoard.getPiece(move.getStartPosition()).getTeamColor() == turn)){
            throw new InvalidMoveException("Invalid move!");}

        else if(validMoves(move.getStartPosition()) != null){
            if(validMoves(move.getStartPosition()).contains(move)){

                if(move.getPromotionPiece() != null){
                    ChessPiece currentPiece =
                            new ChessPiece(gameBoard.getPiece(move.getStartPosition()).getTeamColor(),move.getPromotionPiece());
                    gameBoard.addPiece(move.getEndPosition(), currentPiece); //adds piece to desired end position
                    gameBoard.addPiece(move.getStartPosition(), null);
                }else {
                    gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
                    gameBoard.addPiece(move.getStartPosition(), null); // sets previous position to null piece
                }

                // Change turns when move is executed
                if(gameBoard.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.WHITE) {
                    setTeamTurn(TeamColor.BLACK);
                }
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
    //iterate through all the opposite colored pieces.
    // if the piece contains the same position as the king of current team color, return true
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> otherTeamMoves = new ArrayList<ChessMove>();
        ChessPosition currentTeamsKing = null;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentGamePiece = gameBoard.getPiece(currentPosition);

                currentTeamsKing =
                        getCurrentPosition
                                (teamColor, currentGamePiece, otherTeamMoves, currentPosition, currentTeamsKing, i, j);
            }
        }
        for(ChessMove element: otherTeamMoves){
            if(element.getEndPosition().getRow() == currentTeamsKing.getRow()
                    && element.getEndPosition().getColumn() == currentTeamsKing.getColumn()){
                return true;
            }
        }
        return false;
    }

    private ChessPosition getCurrentPosition
            (TeamColor teamColor,
             ChessPiece currentGamePiece,
             Collection<ChessMove> otherTeamMoves,
             ChessPosition currentPosition,
             ChessPosition currentTeamsKingPosition,
             int i, int j) {
        if(currentGamePiece != null){
            if(currentGamePiece.getTeamColor() != teamColor){
                otherTeamMoves.addAll(currentGamePiece.pieceMoves(gameBoard, currentPosition));
            }
            else if(currentGamePiece.getPieceType() == ChessPiece.PieceType.KING
                    && (currentGamePiece.getTeamColor() == teamColor)){
                currentTeamsKingPosition = new ChessPosition(i, j);
            }
        }
        return currentTeamsKingPosition;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> outOfCheckMoves = new ArrayList<ChessMove>();
        ChessPosition currentTeamsKingPosition = null;
        for(int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                if(gameBoard.getPiece(currentPosition)!=null){
                    ChessPiece currentGamePiece = gameBoard.getPiece(currentPosition);
                    if (currentGamePiece.getPieceType() == ChessPiece.PieceType.KING
                            && (currentGamePiece.getTeamColor() == teamColor)){
                        currentTeamsKingPosition = currentPosition;
                        outOfCheckMoves.addAll(validMoves(currentTeamsKingPosition));
                    }else if(currentGamePiece.getTeamColor() == teamColor){
                        outOfCheckMoves.addAll(validMoves(currentPosition));
                    }
                }
            }
        }
        isCheckmate = outOfCheckMoves.isEmpty();
        return outOfCheckMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> outOfAllMoves = new ArrayList<ChessMove>();
        for(int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                if(gameBoard.getPiece(currentPosition)!=null){
                    ChessPiece currentGamePiece = gameBoard.getPiece(currentPosition);
                    if (isInCheck(teamColor)){
                        return false;
                    }
                    else if(currentGamePiece.getTeamColor() == teamColor){
                        outOfAllMoves.addAll(validMoves(currentPosition));
                    }
                }
            }
        }isStalemate = outOfAllMoves.isEmpty();
        return outOfAllMoves.isEmpty();
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
