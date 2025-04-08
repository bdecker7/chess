package records;

import websocket.messages.ServerMessage;

public record ServerMessageType(ServerMessage.ServerMessageType type, String message) {
}
