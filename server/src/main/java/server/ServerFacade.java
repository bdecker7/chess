package server;

import records.*;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    public RegisterResult register(RegisterRequest request){


        return new RegisterResult(null,null);
    }
    public LoginResult login(LoginRequest request){

        return new LoginResult(null,null);
    }
    public void logout(LogOutRequest request){

    }
    public ListGameResult listGames(ListGameRequest request){

        //change parameters
        return new ListGameResult(null);
    }
    public CreateGameResult createGame(CreateGameRequest request){

        return new CreateGameResult(0); //change parameters
    }

//    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
//        try {
//            URL url = (new URI(serverUrl + path)).toURL();
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setRequestMethod(method);
//            http.setDoOutput(true);
//
//            writeBody(request, http);
//            http.connect();
//            throwIfNotSuccessful(http);
//            return readBody(http, responseClass);
//        } catch (ResponseException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}
