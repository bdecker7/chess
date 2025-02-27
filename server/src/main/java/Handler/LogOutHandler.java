package Handler;


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

    public String handleLogOutRequest(Request req, Response res) throws UnAuthorizedException, ServerMalfunctionException{

        try{
            String request = new Gson().fromJson(req.headers("authorization"), String.class);   //gets json to a request object
            UserService service = new UserService(memoryDAO,authDAO);
            service.logout(request);
            res.status(200);
            return "{}";
        }
        catch(UnAuthorizedException e){
            res.status(400);
            return new Gson().toJson(e.getMessage());
        }
        catch(ServerMalfunctionException e){
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }

    }

}

