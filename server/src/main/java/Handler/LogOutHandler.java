package Handler;

import Service.EmptyResponse;
import Service.RegisterResult;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.*;
import spark.Request;
import spark.Response;

public class LogOutHandler {
    UserDAO memoryDAO;
    AuthDAO authDAO;
    public LogOutHandler(UserDAO memoryDAO, AuthDAO authDAO){
        this.memoryDAO = memoryDAO;
        this.authDAO = authDAO;
    }

    public String handleLogOutRequest(Request req, Response res) throws UnAuthorizedException{

        try{
            //not sure what to do here.

//            LogOutRequest request = new Gson().fromJson(req.body(), LogOutRequest.class);   //gets json to a request object
//            UserService service = new UserService(memoryDAO,authDAO);
//            service.logout(req);
            res.status(200);
            EmptyResponse nothing = new EmptyResponse();
            return new Gson().toJson(nothing);
        }
        catch(UnAuthorizedException e){
            res.status(400);
            return new Gson().toJson(e.getMessage());
        }

        // put the 500 error in here?

    }

}

