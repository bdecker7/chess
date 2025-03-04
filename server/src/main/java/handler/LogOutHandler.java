package handler;


import dataaccess.exceptions.ServerMalfunctionException;
import dataaccess.exceptions.UnAuthorizedException;
import records.ErrorRecordClass;
import service.UserService;
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

    public String handleLogOutRequest(Request req, Response res) throws UnAuthorizedException, ServerMalfunctionException {

        try{
            String request = req.headers("authorization");   //gets json to a request object
            UserService service = new UserService(memoryDAO,authDAO);
            service.logout(request);
            res.status(200);
            return "{}";
        }
        catch(UnAuthorizedException e){
            res.status(401);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }
        catch(Error e){
            res.status(500);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }

    }

}

