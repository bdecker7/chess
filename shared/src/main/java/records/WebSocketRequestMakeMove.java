package records;

import chess.ChessMove;
import com.google.gson.Gson;

public record WebSocketRequestMakeMove(Type type, String authToken, int gameID, ChessMove move) {
    public enum Type {
        MAKEMOVE
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}