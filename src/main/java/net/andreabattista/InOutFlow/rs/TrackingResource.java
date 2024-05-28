package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.TrackingManager;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.dto.TrackingDto;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("/tracking")
public class TrackingResource {

    @GET
    @Path("/")
    public Response getByUser(@HeaderParam("Authorization") String token) {
        try {
            List<TrackingDto> tracking = TrackingManager.getByEmployee(token);
            return ResourceUtility.buildOkResponse(tracking);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }
    
    @GET
    @Path("/percentages")
    public Response getPercentagesData(@HeaderParam("Authorization") String token) {
        try {
            List<TrackingDto> dto = TrackingManager.getByEmployee(token);
            int[] data = TrackingManager.getWeekData(dto);
            return ResourceUtility.buildOkResponse(data);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }
}
