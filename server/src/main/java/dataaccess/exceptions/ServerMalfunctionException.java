package dataaccess.exceptions;

public class ServerMalfunctionException extends RuntimeException {
    public ServerMalfunctionException(String message) {
        super(message);
    }
}
