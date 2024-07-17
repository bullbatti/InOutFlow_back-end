package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.CompanyManager;
import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.dto.CompanyDto;
import net.andreabattista.InOutFlow.dto.MessageDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * RESTful resource class for managing companies.
 * Provides endpoints for retrieving, creating, modifying, and deleting companies.
 * Each method handles a specific HTTP request type and interacts with the CompanyManager to perform the required operations.
 * Responses are built using the ResourceUtility class to ensure consistent response formatting.
 *
 * @author bullbatti
 */
@Path("/companies")
public class CompanyResource {

    /**
     * Handles HTTP GET requests to retrieve all companies.
     * Calls the CompanyManager's getAll method to fetch the list of companies.
     * Builds and returns an HTTP OK response with the list of companies if successful.
     * If an exception occurs, builds and returns an HTTP Bad Request response with the error message.
     *
     * @return a Response object containing the list of companies or an error message.
     */
    @GET
    @Path("/")
    public Response getAll(@HeaderParam("Authorization") String token) {
        try {
            LoginManager.checkTokenValidation(token);
            List<CompanyDto> companies = CompanyManager.getAll();
            return ResourceUtility.buildOkResponse(companies);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @GET
    @Path("/employee")
    public Response geByLoggedEmployee(@HeaderParam("Authorization") String token) {
        try {
            LoginManager.checkTokenValidation(token);
            CompanyDto company = CompanyManager.getByLoggedEmployee(token);
            return ResourceUtility.buildOkResponse(company);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    @POST
    @Path("/message")
    public Response geByLoggedEmployee(@HeaderParam("Authorization") String token, MessageDto message) {
        try {
            LoginManager.checkTokenValidation(token);
            CompanyDto company = CompanyManager.getByMessage(message);
            return ResourceUtility.buildOkResponse(company);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    /**
     * Handles HTTP POST requests to create a new company.
     * Consumes a JSON representation of a CompanyDto object.
     * Validates the provided authorization token and creates the company.
     * Returns an HTTP OK response with the updated list of companies if successful.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token the authorization token for validating the request.
     * @param companyDto the CompanyDto object containing the details of the company to be created.
     * @return a Response object containing the updated list of companies or an error message.
     */
    @POST
    @Path("/new")
    @Consumes("application/json")
    public Response create(@HeaderParam("Authorization") String token, CompanyDto companyDto) {
        try {
            LoginManager.checkTokenValidation(token);
            List<CompanyDto> companies = CompanyManager.create(companyDto);
            return ResourceUtility.buildOkResponse(companies);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    /**
     * Handles HTTP PUT requests to modify an existing company.
     * Consumes a JSON representation of a CompanyDto object.
     * Validates the provided authorization token and modifies the company details.
     * Returns an HTTP OK response with the updated list of companies if successful.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token the authorization token for validating the request.
     * @param companyDto the CompanyDto object containing the updated details of the company.
     * @return a Response object containing the updated list of companies or an error message.
     */
    @PUT
    @Path("/modify")
    @Consumes("application/json")
    public Response modify(@HeaderParam("Authorization") String token, CompanyDto companyDto) {
        try {
            LoginManager.checkTokenValidation(token);
            List<CompanyDto> companies = CompanyManager.modify(companyDto);
            return ResourceUtility.buildOkResponse(companies);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }

    /**
     * Handles HTTP DELETE requests to delete specified companies.
     * Consumes a JSON representation of a list of CompanyDto objects.
     * Validates the provided authorization token and deletes the specified companies.
     * Returns an HTTP OK response with the updated list of companies if successful.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token the authorization token for validating the request.
     * @param companies the list of CompanyDto objects representing the companies to be deleted.
     * @return a Response object containing the updated list of companies or an error message.
     */
    @DELETE
    @Path("/delete")
    @Consumes("application/json")
    public Response delete(@HeaderParam("Authorization") String token, List<CompanyDto> companies) {
        try {
            LoginManager.checkTokenValidation(token);
            List<CompanyDto> res = CompanyManager.delete(companies);
            return ResourceUtility.buildOkResponse(res);
        } catch (Exception e) {
            return ResourceUtility.buildBadResponse(e.getMessage());
        }
    }
}
