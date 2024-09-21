package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.business.MainSmartCard;
import net.andreabattista.InOutFlow.business.SmartCardManager;
import net.andreabattista.InOutFlow.business.SmartCardReader;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.dto.ModifySmartCardDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/smart-cards")
public class SmartCardResource {

    @GET
    @Path("/read-id")
    public Response readId(@HeaderParam("Authorization") String token) throws Exception {
        LoginManager.checkTokenValidation(token);
        String id = SmartCardManager.readOnlyNewSmartCard();
        return ResourceUtility.buildOkResponse(id);
    }

    @POST
    @Path("/id/")
    @Consumes("application/json")
    public Response getId(@HeaderParam("Authorization") String token, EmployeeDto employeeDto) {
        LoginManager.checkTokenValidation(token);
        String id = SmartCardManager.getIdByEmployee(employeeDto);
        return ResourceUtility.buildOkResponse(id);
    }

    @PUT
    @Path("/modify")
    @Consumes("application/json")
    public Response getId(@HeaderParam("Authorization") String token, ModifySmartCardDto dto) {
        LoginManager.checkTokenValidation(token);
        boolean res = SmartCardManager.modify(dto);
        return ResourceUtility.buildOkResponse(res);
    }
}
