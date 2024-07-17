package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.business.MessageManager;
import net.andreabattista.InOutFlow.dto.MessageDto;
import net.andreabattista.InOutFlow.dto.MessageSentDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/messages")
public class MessageResource {

    @GET
    @Path("/receiver")
    public Response getMessagesToRead(@HeaderParam("Authorization") String token) {
        try {
            LoginManager.checkTokenValidation(token);
            List<MessageDto> messages = MessageManager.getMessagesToRead(token);
            return ResourceUtility.buildOkResponse(messages);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @POST
    @Path("/new")
    public Response saveMessage(@HeaderParam("Authorization") String token, MessageSentDto message) {
        try {
            LoginManager.checkTokenValidation(token);
            boolean res = MessageManager.saveMessage(message);
            return ResourceUtility.buildOkResponse(res);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @PUT
    @Path("/completed")
    public Response setMessageToCompleted(@HeaderParam("Authorization") String token, MessageDto message) {
        try {
            LoginManager.checkTokenValidation(token);
            boolean res = MessageManager.setMessageToCompleted(message);
            return ResourceUtility.buildOkResponse(res);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }
}
