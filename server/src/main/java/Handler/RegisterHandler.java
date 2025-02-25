package Handler;


import Service.RegisterResult;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public class RegisterHandler{
    public RegisterHandler(){
    }


    public RegisterResult handleRequest(Request req, Response res) throws DataAccessException, AlreadyTakenException {
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);   //gets json to a request object

        UserService service = new UserService();
        try{
        RegisterResult result = service.register(request);
        }
        catch(DataAccessException e){
            res.status(400);
        }
        catch(AlreadyTakenException e){
            res.status(403);
        }
        return Gson.toJson(result);
    }

}
