import chess.*;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.exceptions.DataAccessException;
import spark.Spark;
import server.Server;

import java.sql.SQLException;

import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();

        try{
            server.run(8080);
        }
        catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        ;

        // Add a shutdown hook to stop the server when the application is terminated
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Server stopped.");
        }));

        System.out.println("Server is running on port: " + Spark.port());

    }
}