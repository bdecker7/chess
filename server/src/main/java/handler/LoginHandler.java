package handler;

import records.ErrorRecordClass;
import records.LoginRequest;
import records.LoginResult;
import service.UserService;
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

    public String handleLoginRequest(Request req, Response res)
            throws UnAuthorizedException, ServerMalfunctionException, AlreadyAuthorizedException {

        try {
            LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
            UserService service = new UserService(memoryDAO, authDAO);
            LoginResult result = service.login(request);
            res.status(200);
            return new Gson().toJson(result);
        } catch (UnAuthorizedException | AlreadyAuthorizedException e) {
            res.status(401);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        } catch(Error e){
            res.status(500);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }

    }
}
