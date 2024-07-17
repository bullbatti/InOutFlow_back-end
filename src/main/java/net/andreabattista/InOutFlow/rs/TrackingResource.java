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
        try {
            LoginManager.checkTokenValidation(token);
            LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            List<TrackingDto> tracking = TrackingManager.getByEmployeeAndDate(token, date);
            return ResourceUtility.buildOkResponse(tracking);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @POST
    @Consumes("text/plain")
    @Path("/employee-to-modify/{date}")
    public Response getByEmployeeToModifyAndDate(@HeaderParam("Authorization") String token, @PathParam("date") String dateStr, String email) {
        try {
            LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LoginManager.checkTokenValidation(token);
            List<TrackingDto> tracking = TrackingManager.getByEmployeeToModifyAndDate(email, date);
            return ResourceUtility.buildOkResponse(tracking);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }
    
    @GET
    @Path("/last-week-percentages")
    public Response getLastWeekData(@HeaderParam("Authorization") String token) {
        try {
            int[] weekData = TrackingManager.getLastWeekData(token);
            return ResourceUtility.buildOkResponse(weekData);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }
    
    @GET
    @Path("/year-percentages")
    public Response getLastYearData(@HeaderParam("Authorization") String token) {
        try {
            ArrayList<Integer[]> yearData = TrackingManager.getCurrentYearData(token);
            return ResourceUtility.buildOkResponse(yearData);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @POST
    @Path("/new")
    @Consumes("application/json")
    public Response create(@HeaderParam("Authorization") String token, DeleteAndCreateTrackingDto dto) {
        try {
            LoginManager.checkTokenValidation(token);
            boolean res = TrackingManager.create(dto);
            return ResourceUtility.buildOkResponse(res);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @PUT
    @Path("/modify")
    @Consumes("application/json")
    public Response modifyTracking(@HeaderParam("Authorization") String token, ModifyTrackingEmployeeDto combined) {
        try {
            LoginManager.checkTokenValidation(token);
            boolean res = TrackingManager.modifyEvent(combined);
            return ResourceUtility.buildOkResponse(res);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public Response modifyTracking(@HeaderParam("Authorization") String token, DeleteAndCreateTrackingDto deleteAndCreateTrackingDto) {
        try {
            LoginManager.checkTokenValidation(token);
            boolean res = TrackingManager.delete(deleteAndCreateTrackingDto);
            return ResourceUtility.buildOkResponse(res);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }
}
