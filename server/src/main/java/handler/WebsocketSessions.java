package handler;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketSessions {
    Integer gameID;
    HashMap<Integer, HashMap<String,Session>> sessionMap = new HashMap<>();
    public WebsocketSessions(){

    }
    //hashmap with username and authtoken inside hash set
    public void addSessionToGame(Integer gameID,Session session, String authToken){
        HashMap<String,Session> finalSessions = new HashMap<String,Session>();

        if(sessionMap.containsKey(gameID)){
            //grabs the hashmap
            HashMap<String,Session> sessions = sessionMap.get(gameID);
            sessions.put(authToken,session);
            sessionMap.put(gameID,sessions);

        }else{
            finalSessions.put(authToken,session);
            sessionMap.put(gameID,finalSessions);}
    }
    public void removeSessionToGame(Integer gameID,Session session,String authToken){
        HashSet<Session> finalSessions = new HashSet<Session>();
        if(sessionMap.containsKey(gameID)){

            HashMap<String, Session> sessionsToKeep = sessionMap.get(gameID);
            sessionsToKeep.remove(sessionsToKeep.remove(authToken));

            sessionMap.replace(gameID,sessionsToKeep);
        }else {
            sessionMap.remove(gameID); //check this functionality
        }
//            sessionMap.put(gameID,finalSessions);}
    }
    public HashMap<String, Session> getSession(Integer gameID){
        return sessionMap.get(gameID);
    }


}
