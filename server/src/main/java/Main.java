import chess.*;
import spark.Spark;
import server.Server;
import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        server.run(8080);

        // Add a shutdown hook to stop the server when the application is terminated
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Server stopped.");
        }));

        System.out.println("Server is running on port: " + Spark.port());

    }
}