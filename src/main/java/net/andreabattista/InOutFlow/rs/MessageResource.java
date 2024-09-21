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
        LoginManager.checkTokenValidation(token);
        List<MessageDto> messages = MessageManager.getMessagesToRead(token);
        return ResourceUtility.buildOkResponse(messages);
    }

    @POST
    @Path("/new")
    public Response saveMessage(@HeaderParam("Authorization") String token, MessageSentDto message) {
        LoginManager.checkTokenValidation(token);
        boolean res = MessageManager.saveMessage(message);
        return ResourceUtility.buildOkResponse(res);
    }

    @PUT
    @Path("/completed")
    public Response setMessageToCompleted(@HeaderParam("Authorization") String token, MessageDto message) {
        LoginManager.checkTokenValidation(token);
        boolean res = MessageManager.setMessageToCompleted(message);
        return ResourceUtility.buildOkResponse(res);
    }
}
