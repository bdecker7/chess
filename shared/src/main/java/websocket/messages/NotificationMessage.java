package websocket.messages;

public class NotificationMessage extends ServerMessage{
    String notificationMessage;
    public NotificationMessage(ServerMessageType type, String notificationMessage) {
        super(type);
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationMessage(){
        return this.notificationMessage;
    }
}
