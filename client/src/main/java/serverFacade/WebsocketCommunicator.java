package serverFacade;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import records.WebSocketRecords;
import records.WebSocketRequestMakeMove;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebsocketCommunicator extends Endpoint {


    Session session;
    ServerMessageObserver notificationHandler;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void WebSocketFacade(String url, ServerMessageObserver notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    //check which type by deserializing
                    //deserialize it again.
                    ServerMessage command = new Gson().fromJson(message,ServerMessage.class);
                    if(command.getServerMessageType() == LOAD_GAME){
                        LoadGameMessage loadMessage = new Gson().fromJson(message,LoadGameMessage.class);
                        loadMessage.getServerMessageString(); //this is the game data
                        System.out.println(">>> Load Game");
                    }else if(command.getServerMessageType() == NOTIFICATION){
                        Notification notification = new Gson().fromJson(message, Notification.class);
                        notificationHandler.notify(notification);
                    }else if(command.getServerMessageType() == ERROR){
                        ErrorMessage errorMessage = new Gson().fromJson(message,ErrorMessage.class);
                        System.out.println(errorMessage); // check this one
                    }

                }
            });
            } catch ( URISyntaxException | DeploymentException | IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void connectClient(String authToken, int gameID) throws IOException {
        WebSocketRecords resignObject = new WebSocketRecords(UserGameCommand.CommandType.CONNECT,authToken,gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(resignObject));

    }

    public void makeMoveWs(String authToken, int gameID, ChessMove moveObject) throws IOException {
        //send over the json i think
        WebSocketRequestMakeMove makeMoveObject = new WebSocketRequestMakeMove(UserGameCommand.CommandType.MAKE_MOVE,authToken,gameID,moveObject);
        this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveObject));
        //reprint the board.
    }

    public void leaveWs(String authToken, int gameID) throws IOException {

        WebSocketRecords leavingObject = new WebSocketRecords(UserGameCommand.CommandType.LEAVE,authToken,gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(leavingObject));
        this.session.close();
    }

    public void resignWs(String authToken, int gameID) throws IOException {
        WebSocketRecords resignObject = new WebSocketRecords(UserGameCommand.CommandType.RESIGN,authToken,gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(resignObject));

    }


}
