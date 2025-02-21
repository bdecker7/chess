package Handler;

import chess.ChessGame;
import chess.ChessPiece;

public record joinGameRequest(int gameID, ChessGame.TeamColor playerColor) {
}
