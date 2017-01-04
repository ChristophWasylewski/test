package at.ac.tuwien.qs.movierental;

/**
 * Checkstyle :
 * import wird nicht benötigt -->import java.io.IOException;
 * daher wird er entfernt
 */



public class ServiceNotAvailableException extends Exception {
    public ServiceNotAvailableException(String message) {
        super(message);
    }

    public ServiceNotAvailableException() {
        super();
    }

    public ServiceNotAvailableException(Throwable t) {
        super(t);
    }
}
