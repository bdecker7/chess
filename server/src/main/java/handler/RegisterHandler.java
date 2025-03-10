package handler;


import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ServerMalfunctionException;
import records.ErrorRecordClass;
import records.RegisterRequest;
import records.RegisterResult;
import service.UserService;
import com.google.gson.Gson;
import dataaccess.*;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class RegisterHandler{
    UserDAO memoryDAO;
    AuthDAO authDAO;
    public RegisterHandler(UserDAO memoryDAO, AuthDAO authDAO){
        this.memoryDAO = memoryDAO;
        this.authDAO = authDAO;
    }

    public String handleRequest(Request req, Response res) throws DataAccessException, AlreadyTakenException, ServerMalfunctionException {

        try{
            RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);   //gets json to a request object
            UserService service = new UserService(memoryDAO,authDAO);
            RegisterResult result = service.register(request);
            res.status(200);
            return new Gson().toJson(result);
        }
        catch(DataAccessException e){
            res.status(400);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }
        catch(AlreadyTakenException e){
            res.status(403);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        }
        catch(Error e){
            res.status(500);
            ErrorRecordClass error = new ErrorRecordClass(e.getMessage());
            return new Gson().toJson(error);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
