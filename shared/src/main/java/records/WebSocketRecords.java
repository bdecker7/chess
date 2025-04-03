package records;

import chess.ChessMove;

public record WebSocketRecords(WebSocketRecords.Type type, String authToken, int gameID) {
    public enum Type {
        LEAVE,
        RESIGN,
        CONNECT
    }
}
