package net.andreabattista.InOutFlow.business;

/**
 * Represents an exception that indicates validation failure.
 * This exception extends {@link RuntimeException}, making it an unchecked exception.
 *
 * @author bullbatti
 */
public class ValidationException extends RuntimeException{
    
    public ValidationException() {}
    
    public ValidationException(String message) {
        super(message);
    }
}
