package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.business.TrackingManager;

import net.andreabattista.InOutFlow.dto.DeleteAndCreateTrackingDto;
import net.andreabattista.InOutFlow.dto.ModifyTrackingEmployeeDto;
import net.andreabattista.InOutFlow.dto.TrackingDto;
import net.andreabattista.InOutFlow.model.Login;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * REST resource for handling tracking data and operations.
 *
 * @author bullbatti
 */
@Path("/tracking")
public class TrackingResource {

    /**
     * Endpoint for retrieving the traces of an employee for a given date.
     *
     * @param token The authentication token provided in the request header
     * @return A response containing the employee information.
     */
    @GET
    @Path("/{date}")
    public Response getByEmployeeAndDate(@HeaderParam("Authorization") String token, @PathParam("date") String dateStr) {
        LoginManager.checkTokenValidation(token);
        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        List<TrackingDto> tracking = TrackingManager.getByEmployeeAndDate(token, date);
        return ResourceUtility.buildOkResponse(tracking);
    }

    @POST
    @Consumes("text/plain")
    @Path("/employee-to-modify/{date}")
    public Response getByEmployeeToModifyAndDate(@HeaderParam("Authorization") String token, @PathParam("date") String dateStr, String email) {
        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        LoginManager.checkTokenValidation(token);
        List<TrackingDto> tracking = TrackingManager.getByEmployeeToModifyAndDate(email, date);
        return ResourceUtility.buildOkResponse(tracking);
    }

    @GET
    @Path("/last-week-percentages")
    public Response getLastWeekData(@HeaderParam("Authorization") String token) {
        int[] weekData = TrackingManager.getLastWeekData(token);
        return ResourceUtility.buildOkResponse(weekData);
    }

    @GET
    @Path("/year-percentages")
    public Response getLastYearData(@HeaderParam("Authorization") String token) {
        ArrayList<Integer[]> yearData = TrackingManager.getCurrentYearData(token);
        return ResourceUtility.buildOkResponse(yearData);
    }

    @POST
    @Path("/new")
    @Consumes("application/json")
    public Response create(@HeaderParam("Authorization") String token, DeleteAndCreateTrackingDto dto) {
        LoginManager.checkTokenValidation(token);
        boolean res = TrackingManager.create(dto);
        return ResourceUtility.buildOkResponse(res);
    }

    @PUT
    @Path("/modify")
    @Consumes("application/json")
    public Response modifyTracking(@HeaderParam("Authorization") String token, ModifyTrackingEmployeeDto combined) {
        LoginManager.checkTokenValidation(token);
        boolean res = TrackingManager.modifyEvent(combined);
        return ResourceUtility.buildOkResponse(res);
    }

    @DELETE
    @Path("/")
    public Response modifyTracking(@HeaderParam("Authorization") String token, DeleteAndCreateTrackingDto deleteAndCreateTrackingDto) {
        LoginManager.checkTokenValidation(token);
        boolean res = TrackingManager.delete(deleteAndCreateTrackingDto);
        return ResourceUtility.buildOkResponse(res);
    }
}
