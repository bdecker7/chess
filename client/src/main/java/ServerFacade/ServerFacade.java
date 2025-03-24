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
import java.util.Objects;

public class ServerFacade {
    private Integer status;
    private String serverUrl = "http://localhost:8080";
    private String body = "body";
    private String header = "header";
    private String both = "both";
    // make a seperate variable for the server address.

    public RegisterResult register(RegisterRequest request) throws Exception {

        HttpURLConnection http = this.makeRequest("POST", "/user",request, body,null);
        return readBody(http, RegisterResult.class);
    }
    public LoginResult login(LoginRequest request) throws Exception {

        HttpURLConnection http = this.makeRequest("POST","/session",request,body, null);
        return readBody(http,LoginResult.class);
    }
    public Integer logout(LogOutRequest request) throws Exception {

        HttpURLConnection http = this.makeRequest("DELETE","/session",request,header, request.authToken());
        return http.getResponseCode();

    }
    public ListGameResult listGames(ListGameRequest request) throws Exception {

        HttpURLConnection http = this.makeRequest("GET", "/game",request, header, request.authToken());
        return readBody(http, ListGameResult.class);
    }
    public CreateGameResult createGame(CreateGameRequest request) throws Exception {

        HttpURLConnection http = this.makeRequest("PUT","/game",request, both, request.authToken());
        return readBody(http, CreateGameResult.class);

    }
    public Integer joinGame(JoinGameRequest request) throws Exception{
        // not sure where the auth token is for the request
        HttpURLConnection http = this.makeRequest("PUT","/game", request, both, null);
        return http.getResponseCode();
    }

    private <T> HttpURLConnection makeRequest(String method, String path, Object request, String dataRequestPath, String authToken) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if(Objects.equals(dataRequestPath, body)){
                writeBody(request, http);
            }else if(Objects.equals(dataRequestPath, header)){
                http.addRequestProperty("Authorization", authToken);
            }else if(Objects.equals(dataRequestPath, both)){
                http.addRequestProperty("Authorization", authToken);
                writeBody(request, http);
            }

            http.connect();
//            status = throwIfNotSuccessful(http);
            return http;
//            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    private Integer throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status_var = http.getResponseCode();
        if (status_var != 200) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw new Exception("Response Code unsuccessful");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("Not successful response");
        }else{
            return status;
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

    private void writeBody(Object request, HttpURLConnection http) {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
