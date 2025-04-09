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
    public WebsocketSessions(){

    }

    public void addSessionToGame(Integer gameID,Session session){
        Set<Session> finalSessions = new HashSet<Session>();
        if(sessionMap.containsKey(gameID)){
            Set<Session> sessions = sessionMap.get(gameID);
            finalSessions.addAll(sessions);
            finalSessions.add(session);
            sessionMap.put(gameID,finalSessions);
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
            sessionMap.put(gameID,finalSessions);
        }else {
            sessionMap.remove(gameID); //check this functionality
        }
//            sessionMap.put(gameID,finalSessions);}
    }
    public Set<Session> getSession(Integer gameID){
        return sessionMap.get(gameID);
    }


}
