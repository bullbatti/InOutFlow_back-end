package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.dto.LoginDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * REST resource for handling user login operations.
 *
 * @author bullbatti
 */
@Path("/login")
public class LoginResource {

    /**
     * Endpoint for user login. Allows a user to authenticate and obtain an access token.
     *
     * @param dto The login credentials provided by the user
     * @return A response containing the access token upon successful login.
     * If login fails, return an unauthorized response with an error message.
     */
    @POST
    @Path("/")
    @Consumes("application/json")
    public Response login(LoginDto dto) {
        String token = LoginManager.login(dto);
        return ResourceUtility.buildOkHeaderResponse(token);
    }
}
