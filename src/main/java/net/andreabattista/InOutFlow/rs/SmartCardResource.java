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
    public Response readId(@HeaderParam("Authorization") String token) {
        try {
            LoginManager.checkTokenValidation(token);
            String id = SmartCardManager.readOnlyNewSmartCard();
            return ResourceUtility.buildOkResponse(id);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @POST
    @Path("/id/")
    @Consumes("application/json")
    public Response getId(@HeaderParam("Authorization") String token, EmployeeDto employeeDto) {
        try {
            LoginManager.checkTokenValidation(token);
            String id = SmartCardManager.getIdByEmployee(employeeDto);
            return ResourceUtility.buildOkResponse(id);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @PUT
    @Path("/modify")
    @Consumes("application/json")
    public Response getId(@HeaderParam("Authorization") String token, ModifySmartCardDto dto) {
        try {
            LoginManager.checkTokenValidation(token);
            boolean res = SmartCardManager.modify(dto);
            return ResourceUtility.buildOkResponse(res);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }
}
