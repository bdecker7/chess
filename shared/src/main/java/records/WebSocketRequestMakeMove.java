package records;

import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

public record WebSocketRequestMakeMove(UserGameCommand.CommandType type, String authToken, int gameID, ChessMove move) {
}