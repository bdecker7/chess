package records;

public record WebSocketRecords(websocket.commands.UserGameCommand.CommandType type, String authToken, int gameID) {
    public enum Type {
        LEAVE,
        RESIGN,
        CONNECT
    }
}
