package dataaccess.exceptions;

public class AlreadyAuthorizedException extends RuntimeException {
    public AlreadyAuthorizedException(String message) {
        super(message);
    }
}
