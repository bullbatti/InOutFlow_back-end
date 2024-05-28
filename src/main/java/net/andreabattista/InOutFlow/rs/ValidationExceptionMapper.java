package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.ValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps instances of {@link ValidationException} to HTTP responses.
 * This class implements {@link javax.ws.rs.ext.ExceptionMapper} to handle instances
 * of ValidationException and convert them into appropriate HTTP responses.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    
    /**
     * Maps a ValidationException to an HTTP response.
     *
     * @param e The ValidationException instance to be mapped.
     * @return The HTTP response representing the mapped exception.
     */
    @Override
    public Response toResponse(ValidationException e) {
        return ResourceUtility.buildUnauthoraizedResponse(e.getMessage());
    }
}
