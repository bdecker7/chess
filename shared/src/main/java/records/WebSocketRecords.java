package records;

public record WebSocketRecords(websocket.commands.UserGameCommand.CommandType commandType, String authToken, int gameID) {
    public enum Type {
        LEAVE,
        RESIGN,
        CONNECT
    }
}
