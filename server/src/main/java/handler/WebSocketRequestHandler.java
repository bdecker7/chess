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
import java.util.HashMap;
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

    private void saveSession(Integer gameID, Session session, String authToken) {
        //This should save the session so it keeps track of everything. probably seperate in another class
        savedSessions.addSessionToGame(gameID,session,authToken);

    }

    private void sendMessage(ServerMessage.ServerMessageType type,Integer gameId, Session session, String message)
            throws IOException, DataAccessException {
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

    private void broadcastMessage(ServerMessage.ServerMessageType type ,
                                  Integer gameID, String message, Session notThisSession, String authToken)
            throws IOException, DataAccessException {
        HashMap<String, Session> currentSession = savedSessions.getSession(gameID);
        for(String auth : currentSession.keySet()){
            if(type.equals(LOAD_GAME)){
                if(authToken == null){
                    NotificationMessage messageToSend = new NotificationMessage(type,message);
                    currentSession.get(auth).getRemote().sendString(new Gson().toJson(messageToSend));
                }
                else if(!auth.equals(authToken)){
                    LoadGameMessage messageToSend = new LoadGameMessage(type,gamedata.getGame(gameID));
                    currentSession.get(auth).getRemote().sendString(new Gson().toJson(messageToSend));
                }
            }else if (type.equals(NOTIFICATION)){
                if(authToken == null){
                    NotificationMessage messageToSend = new NotificationMessage(type,message);
                    currentSession.get(auth).getRemote().sendString(new Gson().toJson(messageToSend));
                }
                else if(!auth.equals(authToken)){
                    NotificationMessage messageToSend = new NotificationMessage(type,message);
                    currentSession.get(auth).getRemote().sendString(new Gson().toJson(messageToSend));
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
                ChessGame newer = gamedata.getGame(data.getGameID()).game();
                newer.changeResignedStatus();
                gamedata.updateAfterMove(data.getGameID(), newer);

                broadcastMessage(NOTIFICATION, data.getGameID(), authdata.getAuthUsername(data.getAuthToken()) + " has resigned", null,null);
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

            //THIS WAS THE PROBLEM!!!!!!
            savedSessions.removeSessionToGame(data.getGameID(),session, data.getAuthToken());
            broadcastMessage(NOTIFICATION, data.getGameID(), authdata.getAuthUsername(data.getAuthToken()) + " left the game", session, data.getAuthToken());

        }catch (Exception e){
            sendMessage(ERROR,null,session,"Error:" + e.getMessage());
        }

    }

    private void makeMove(MakeMoveCommand data, Session session) throws DataAccessException, InvalidMoveException, IOException {

        try {
            String username = authdata.getAuthUsername(data.getAuthToken());
            GameData currentGame = gamedata.getGame(data.getGameID());
            if(currentGame.game().getIsResigned()){
//                throw new InvalidMoveException("Game is resigned. No more moves can be made");
                sendMessage(ERROR,null,session,"Error:" + " game has been resigned");
            }else if(currentGame.game().isInCheckmate(ChessGame.TeamColor.WHITE)
                    || currentGame.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
//                throw new InvalidMoveException("Game is in checkmate. No more moves can be made");
                sendMessage(ERROR,null,session,"Error:" + " game has checkmate");
            }else if(currentGame.game().isInStalemate(ChessGame.TeamColor.BLACK)
                    ||currentGame.game().isInStalemate(ChessGame.TeamColor.WHITE)){
//                throw new InvalidMoveException("Game is in stalemate. No more moves can be made");
                sendMessage(ERROR,null,session,"Error:" + " game has stalemate");
            }
            else if((currentGame.game().getTeamTurn().equals(ChessGame.TeamColor.WHITE)
                    && currentGame.whiteUsername().equals(username))
                    || (currentGame.game().getTeamTurn().equals(ChessGame.TeamColor.BLACK)
                    && currentGame.blackUsername().equals(username))) {

                ChessMove move = new ChessMove(data.getMove().getStartPosition(), data.getMove().getEndPosition(), null);
                currentGame.game().makeMove(move);

                gamedata.updateAfterMove(data.getGameID(), currentGame.game());
                GameData updatedGame = gamedata.getGame(data.getGameID());

                if(updatedGame.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
                    broadcastMessage(NOTIFICATION, data.getGameID(), updatedGame.whiteUsername() + " is in CHECKMATE!! Black wins!",null,null);
                }else if(updatedGame.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                    broadcastMessage(NOTIFICATION, data.getGameID(), updatedGame.blackUsername() + " is in CHECKMATE!! White wins!",null,null);
                }else if(updatedGame.game().isInStalemate(ChessGame.TeamColor.BLACK)){
                    broadcastMessage(NOTIFICATION, data.getGameID(), updatedGame.blackUsername()+" is in STALEMATE!! No more moves can be made!",null,null);
                }else if(updatedGame.game().isInStalemate(ChessGame.TeamColor.WHITE)){
                    broadcastMessage(NOTIFICATION, data.getGameID(), updatedGame.whiteUsername()+" is in STALEMATE!! No more moves can be made!",null,null);
                }else if(updatedGame.game().isInCheck(ChessGame.TeamColor.WHITE)){
                    broadcastMessage(NOTIFICATION, data.getGameID(), updatedGame.whiteUsername()+" is in CHECK!!! White is in Check",null,null);
                }else if(updatedGame.game().isInCheck(ChessGame.TeamColor.BLACK)){
                    broadcastMessage(NOTIFICATION, data.getGameID(), updatedGame.blackUsername() + " is in CHECK!! Black is in Check!",null,null);
                }

                //Sends LOAD_GAME message back to everybody
                sendMessage(LOAD_GAME, data.getGameID(), session, null);
                broadcastMessage(LOAD_GAME, data.getGameID(), null, session, data.getAuthToken());

                int column = data.getMove().getStartPosition().getColumn();
                int row = data.getMove().getStartPosition().getRow();
                int column2 = data.getMove().getEndPosition().getColumn();
                int row2 = data.getMove().getEndPosition().getRow();
                String fromColumn = convertToLetter(column);
                String fromRow = Integer.toString(row);
                String fromColumn2 = convertToLetter(column2);
                String fromRow2 = Integer.toString(row2);

                broadcastMessage(NOTIFICATION, data.getGameID(), authdata.getAuthUsername(data.getAuthToken())
                        + " moved " + fromColumn +fromRow+ " to "+fromColumn2+fromRow2, session, data.getAuthToken());

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
            saveSession(data.getGameID(), session,data.getAuthToken());

            sendMessage(LOAD_GAME, data.getGameID(), session, null);
            //Send a LOAD_GAME message back
            //Broadcast to everyone that this client entered with their color or observing status
            String playerPart = "";
            if(Objects.equals(currentGame.whiteUsername(), authdata.getAuthUsername(data.getAuthToken()))){
                playerPart = "white";
            }else if(Objects.equals(currentGame.blackUsername(), username)){
                playerPart = "black";
            }else{
                playerPart = "observer";
            }

            broadcastMessage(NOTIFICATION, currentGame.gameID(), username + " has joined the game" + " as "
                    +playerPart, session, data.getAuthToken());
        }catch (Exception e){
            sendMessage(ERROR,null,session,"Error:" + e.getMessage());
        }
    }
    private String convertToLetter(Integer columnFromString) throws Exception {
        if(columnFromString.equals(1)){
            return "a";
        }else if(columnFromString.equals(2)){
            return "b";
        }else if(columnFromString.equals(3)){
            return "c";
        }else if(columnFromString.equals(4)){
            return "d";
        }else if (columnFromString.equals(5)){
            return "e";
        }else if(columnFromString.equals(6)){
            return "f";
        }else if(columnFromString.equals(7)){
            return "g";
        }else if(columnFromString.equals(8)){
            return "h";
        }else{
            throw new Exception("Invalid color");
        }
    }

}
