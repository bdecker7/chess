package handler;

import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import javax.websocket.*;

@WebSocket
public class WebSocketRequestHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = Serializer.fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, UserGameCommand.CommandType.CONNECT);
                case MAKE_MOVE -> makeMove(session, username, UserGameCommand.CommandType.MAKE_MOVE);
                case LEAVE -> leaveGame(session, username, UserGameCommand.CommandType.LEAVE);
                case RESIGN -> resign(session, username, UserGameCommand.CommandType.RESIGN);
            }
        } catch (UnauthorizedException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void saveSession(Integer gameID, Session session) {
    }

    private void resign(Session session, String username, UserGameCommand.CommandType commandType) {
    }

    private void leaveGame(Session session, String username, UserGameCommand.CommandType commandType) {
    }

    private void makeMove(Session session, String username, UserGameCommand.CommandType commandType) {
    }

    public void connect(Session session, String username,UserGameCommand.CommandType command){

    }

}
