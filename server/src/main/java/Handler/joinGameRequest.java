package Handler;

import chess.ChessGame;
import chess.ChessPiece;

public record joinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
}
