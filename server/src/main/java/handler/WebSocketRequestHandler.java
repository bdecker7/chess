package handler;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnAuthorizedException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import javax.management.Notification;
import javax.websocket.*;
import dataaccess.AuthSQL.*;

import java.sql.SQLException;
import java.util.Objects;

@WebSocket
public class WebSocketRequestHandler {
    AuthDAO authdata;
    GameDAO gamedata;
    UserDAO userdata;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
            if(command.getCommandType().equals(UserGameCommand.CommandType.MAKE_MOVE)){
                MakeMoveCommand moveCommand = new Gson().fromJson(message,MakeMoveCommand.class);
                makeMove(moveCommand);
            }

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(command);
                case LEAVE -> leaveGame(command);
                case RESIGN -> resign(command);
            }
        } catch (UnAuthorizedException ex) {
            // Serializes and sends the error message
//            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
//            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void saveSession(Integer gameID, Session session) {
        //This should save the session so it keeps track of everything. probably seperate in another class
    }

    private void resign(UserGameCommand data) throws DataAccessException {
        gamedata.getGame(data.getGameID()).game().changeResignedStatus(true);

    }

    private void leaveGame(UserGameCommand data) throws DataAccessException, SQLException {

        String username = authdata.getAuthUsername(data.getAuthToken());
        if(Objects.equals(username, gamedata.getGame(data.getGameID()).whiteUsername())){
            gamedata.updateGame(ChessGame.TeamColor.WHITE,null, data.getGameID());
        }else if(Objects.equals(username, gamedata.getGame(data.getGameID()).blackUsername())){
            gamedata.updateGame(ChessGame.TeamColor.BLACK,null, data.getGameID());
        }
        // make new notification
        //broadcast notification here!!!

    }

    private void makeMove(MakeMoveCommand data) throws DataAccessException, InvalidMoveException {
        String username = authdata.getAuthUsername(data.getAuthToken());
        GameData currentGame = gamedata.getGame(data.getGameID());
        ChessMove move = new ChessMove(data.getMove().getStartPosition(),data.getMove().getEndPosition(), null);
        currentGame.game().makeMove(move);

    }

    public void connect(UserGameCommand data){


    }

}
