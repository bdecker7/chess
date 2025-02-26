package Handler;

import Service.LoginResult;
import Service.RegisterResult;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.*;
import spark.Request;
import spark.Response;

public class LoginHandler {
    UserDAO memoryDAO;
    AuthDAO authDAO;

    public LoginHandler(UserDAO memoryDAO, AuthDAO authDAO) {
        this.memoryDAO = memoryDAO;
        this.authDAO = authDAO;
    }

    public String handleLoginRequest(Request req, Response res) throws DataAccessException, UnAuthorizedException {

        try {
            LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);   //gets json to a request object
            UserService service = new UserService(memoryDAO, authDAO);
            LoginResult result = service.login(request);
            res.status(200);
            return new Gson().toJson(result);
        } catch (UnAuthorizedException e) {
            res.status(401);
            return new Gson().toJson(e.getMessage());
        }

        // put the 500 error in here?

    }
}
