package handler;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebsocketSessions {
    Integer gameID;
    Session session;
    HashMap<Integer, Set<Session>> sessionMap = new HashMap<>();
    public WebsocketSessions(Integer gameID, Session session){
        this.gameID = gameID;
        this.session = session;
    }

    public void addSessionToGame(Integer gameID,Session session){
        Set<Session> finalSessions = new HashSet<Session>();
        if(sessionMap.containsKey(gameID)){
            Set<Session> sessions = sessionMap.get(gameID);
            for(Session individualSession : sessions){
                finalSessions.add(individualSession);
                sessionMap.put(gameID,finalSessions);
            }

        }else{
            finalSessions.add(session);
            sessionMap.put(gameID,finalSessions);}
    }
    public void removeSessionToGame(Integer gameID,Session session){
        Set<Session> finalSessions = new HashSet<Session>();
        if(sessionMap.containsKey(gameID)){
            Set<Session> sessions = sessionMap.get(gameID);
            for(Session individualSession : sessions){
                if(!individualSession.equals(session)){
                    finalSessions.add(individualSession);
                    sessionMap.put(gameID,finalSessions);
                }
            }

        }else{
            finalSessions.add(session);
            sessionMap.put(gameID,finalSessions);}
    }
    public Set<Session> getSession(Integer gameID){

    }


}
