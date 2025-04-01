package serverFacade;

import websocket.messages.ServerMessage;

import javax.management.Notification;

public interface ServerMessageObserver {
    void notify(Notification message);
}
