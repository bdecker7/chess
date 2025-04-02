package serverFacade;

import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

import javax.management.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator implements ServerMessageObserver {


    Session session;
    ServerMessageObserver notificationHandler;


    public void WebSocketFacade(String url, ServerMessageObserver notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                Notification notification = new Gson().fromJson(message, Notification.class);
                notificationHandler.notify(notification);
            });
        } catch ( URISyntaxException | DeploymentException | IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }


    @Override
    public void notify(Notification message) {

    }

    public void connectClient(){

    }

    public void makeMoveWs(ChessPosition requestedCurrentPosition, ChessPosition requestedMovingPosition){

    }

    public void leaveWs(){}

    public void resignWs(){}

}
