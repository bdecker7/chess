package dataaccess.exceptions;

public class SQLERROR extends RuntimeException {
    public SQLERROR(String message) {
        super(message);
    }
}
