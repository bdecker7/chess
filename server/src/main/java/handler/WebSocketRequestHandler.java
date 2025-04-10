package handler;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketRequestHandler {
    AuthDAO authdata;
    GameDAO gamedata;
    UserDAO userdata;
    WebsocketSessions savedSessions = new WebsocketSessions();
    public WebSocketRequestHandler(UserDAO userMemory, AuthDAO authMemory, GameDAO gameMemory){
        this.userdata = userMemory;
        this.gamedata = gameMemory;
        this.authdata = authMemory;
        this.savedSessions = savedSessions;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        try {
            UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
            // need to make command type convert from string to actual command type
            UserGameCommand.CommandType commandType = command.getCommandType();
            if(commandType.equals(UserGameCommand.CommandType.MAKE_MOVE)){
                MakeMoveCommand moveCommand = new Gson().fromJson(message,MakeMoveCommand.class);
                makeMove(moveCommand,session);
            }

            switch (command.getCommandType()) {
                case CONNECT -> connect(command,session);
                case LEAVE -> leaveGame(command,session);
                case RESIGN -> resign(command,session);
            }
        } catch (Exception ex) {
            sendMessage(ERROR,null,session,"Error:" + ex.getMessage());
        }
    }

    private void saveSession(Integer gameID, Session session) {
        //This should save the session so it keeps track of everything. probably seperate in another class
        savedSessions.addSessionToGame(gameID,session);

    }

    private void sendMessage(ServerMessage.ServerMessageType type,Integer gameId, Session session, String message) throws IOException, DataAccessException {
        if(type.equals(LOAD_GAME)){
            LoadGameMessage messageToSend = new LoadGameMessage(type,gamedata.getGame(gameId));
            session.getRemote().sendString(new Gson().toJson(messageToSend));
            //need to fix this load game

        }else if(type.equals(NOTIFICATION)){
            NotificationMessage messageToSend = new NotificationMessage(type,message);
            session.getRemote().sendString(new Gson().toJson(messageToSend));
        }else if(type.equals(ERROR)){
            ErrorMessage messageToSend = new ErrorMessage(type,message);
            session.getRemote().sendString(new Gson().toJson(messageToSend));
        }

    }

    private void broadcastMessage(ServerMessage.ServerMessageType type ,Integer gameID, String message, Session notThisSession) throws IOException, DataAccessException {

        for(Session sesh: savedSessions.getSession(gameID)){
            if(type.equals(LOAD_GAME)){
                if(!sesh.equals(notThisSession)){
                    LoadGameMessage messageToSend = new LoadGameMessage(type,gamedata.getGame(gameID));
                    sesh.getRemote().sendString(new Gson().toJson(messageToSend));
                }
            }else if (type.equals(NOTIFICATION)){
                if(!sesh.equals(notThisSession)){
                    NotificationMessage messageToSend = new NotificationMessage(type,message);
                    sesh.getRemote().sendString(new Gson().toJson(messageToSend));
                }
            }
        }
    }

    private void resign(UserGameCommand data, Session session) throws DataAccessException, IOException {
        try {
            if(authdata.getAuthUsername(data.getAuthToken()).equals(gamedata.getGame(data.getGameID()).whiteUsername()) ||
                    authdata.getAuthUsername(data.getAuthToken()).equals(gamedata.getGame(data.getGameID()).blackUsername())) {
                if(gamedata.getGame(data.getGameID()).game().isResigned) {
                    throw new DataAccessException("game already resigned");
                }
                gamedata.getGame(data.getGameID()).game().changeResignedStatus(true);
                broadcastMessage(NOTIFICATION, data.getGameID(), authdata.getAuthUsername(data.getAuthToken()) + " has resigned", null);
            } else{
                throw new DataAccessException("observer can't resign game");
            }
        } catch (Exception e) {
            sendMessage(ERROR,null,session,"Error:" + e.getMessage());
        }
    }

    private void leaveGame(UserGameCommand data, Session session) throws DataAccessException, SQLException, IOException {
        try {
            String username = authdata.getAuthUsername(data.getAuthToken());
            if (Objects.equals(username, gamedata.getGame(data.getGameID()).whiteUsername())) {
                gamedata.updateGame(ChessGame.TeamColor.WHITE, null, data.getGameID());
            } else if (Objects.equals(username, gamedata.getGame(data.getGameID()).blackUsername())) {
                gamedata.updateGame(ChessGame.TeamColor.BLACK, null, data.getGameID());
            }
            savedSessions.removeSessionToGame(data.getGameID(),session);
            broadcastMessage(NOTIFICATION, data.getGameID(), authdata.getAuthUsername(data.getAuthToken()) + " left the game", session);


            //broadcast notification here to everyone in the session!!
        }catch (Exception e){
            sendMessage(ERROR,null,session,"Error:" + e.getMessage());
        }

    }

    private void makeMove(MakeMoveCommand data, Session session) throws DataAccessException, InvalidMoveException, IOException {

        try {
            String username = authdata.getAuthUsername(data.getAuthToken());
            GameData currentGame = gamedata.getGame(data.getGameID());
            if(currentGame.game().isResigned || currentGame.game().isCheckmate || currentGame.game().isStalemate){
                throw new InvalidMoveException("Game is over. No more moves can be made");
            }
            else if((currentGame.game().getTeamTurn().equals(ChessGame.TeamColor.WHITE)
                    && currentGame.whiteUsername().equals(username))
                    || (currentGame.game().getTeamTurn().equals(ChessGame.TeamColor.BLACK)
                    && currentGame.blackUsername().equals(username))) {
                ChessMove move = new ChessMove(data.getMove().getStartPosition(), data.getMove().getEndPosition(), null);
                currentGame.game().makeMove(move);

// implement a new method for this to update the game in the database
                //                gamedata.updateMovedGame()
                //Sends LOAD_GAME message back to everybody
                sendMessage(LOAD_GAME, data.getGameID(), session, null);

                broadcastMessage(LOAD_GAME, data.getGameID(), null, session);
                //broadcasts NOTIFICATION of what move was made
                broadcastMessage(NOTIFICATION, data.getGameID(), authdata.getAuthUsername(data.getAuthToken()) + " moved" + data.getMove(), session);
                //ifCheck, stalemate, or checkmate, broadcast and client get message.
            }else{
                throw new InvalidMoveException("Not your turn");
            }
        }catch (Exception e) {
            sendMessage(ERROR,null,session,"Error:" + e.getMessage());
        }

    }

    public void connect(UserGameCommand data, Session session) throws DataAccessException, IOException {
        try {
            String username = authdata.getAuthUsername(data.getAuthToken());
            GameData currentGame = gamedata.getGame(data.getGameID());
            saveSession(data.getGameID(), session);

            sendMessage(LOAD_GAME, data.getGameID(), session, null);
            //Send a LOAD_GAME message back
            //Broadcast to everyone that this client entered with their color or observing status
            broadcastMessage(NOTIFICATION, currentGame.gameID(), username + " has joined the game", session);
        }catch (Exception e){
            sendMessage(ERROR,null,session,"Error:" + e.getMessage());
        }
    }

}
