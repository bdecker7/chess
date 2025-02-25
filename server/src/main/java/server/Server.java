package server;

import Handler.RegisterHandler;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        MemoryUserDAO UsersMemory = new MemoryUserDAO();
        MemoryAuthDAO AuthsMemory = new MemoryAuthDAO();
        MemoryGameDAO GamesMemory = new MemoryGameDAO();
        //this is the register request
        Spark.post("/user", (req, res) ->
                new RegisterHandler(UsersMemory,AuthsMemory).handleRequest(req,res));


        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
