package ServerFacade;

import com.google.gson.Gson;
import records.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private String serverUrl = "http://localhost:8080";
    // make a seperate variable for the server address.

    public RegisterResult register(RegisterRequest request) throws Exception {

        return this.makeRequest("POST", "/user",request, RegisterResult.class);
    }
    public LoginResult login(LoginRequest request) throws Exception {

        return this.makeRequest("POST","/session",request,LoginResult.class);
    }
    public void logout(LogOutRequest request) throws Exception {

        this.makeRequest("DELETE","/session",request,null);
    }
    public ListGameResult listGames(ListGameRequest request) throws Exception {

        return this.makeRequest("GET", "/game",request, ListGameResult.class);
    }
    public CreateGameResult createGame(CreateGameRequest request) throws Exception {

        return this.makeRequest("PUT","/game",request, CreateGameResult.class);
    }
    public void joinGame(JoinGameRequest request) throws Exception{

        this.makeRequest("PUT","/game", request, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception("Invalid http request");
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (status != 200) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw new Exception("Response Code unsuccessful");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("Not successful response");
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;

    }

    private String writeBody(Object request, HttpURLConnection http) {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
                return reqData;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }return "";
    }
}
