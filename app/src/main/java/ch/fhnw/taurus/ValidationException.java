package ch.fhnw.taurus;

/**
 * Created by nozdormu on 04/05/2016.
 */
public class ValidationException extends Exception{
    public ValidationException() {
    }

    public ValidationException(String detailMessage) {
        super(detailMessage);
    }

    public ValidationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ValidationException(Throwable throwable) {
        super(throwable);
    }
}
