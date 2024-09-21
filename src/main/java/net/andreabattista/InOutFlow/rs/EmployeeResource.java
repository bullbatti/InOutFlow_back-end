package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.business.EmployeeManager;
import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.business.ValidationException;
import net.andreabattista.InOutFlow.dto.CompanyDto;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.dto.EmployeeToInsertDto;
import net.andreabattista.InOutFlow.dto.MessageDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * RESTful resource class for managing employees.
 * Provides endpoints for retrieving, creating, and modifying employee details.
 * Each method handles a specific HTTP request type and interacts with the EmployeeManager to perform the required operations.
 * Responses are built using the ResourceUtility class to ensure consistent response formatting.
 *
 * @author bullbatti
 */
@Path("/employees")
public class EmployeeResource {

    /**
     * Handles HTTP GET requests to retrieve an employee's details based on the provided authorization token.
     * Validates the provided token and retrieves the corresponding EmployeeDto.
     * Returns an HTTP OK response with the EmployeeDto if successful.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token the authorization token for validating the request.
     * @return a Response object containing the EmployeeDto or an error message.
     */
    @GET
    @Path("/")
    public Response getByToken(@HeaderParam("Authorization") String token) {
        LoginManager.checkTokenValidation(token);
        EmployeeDto employeeDto = EmployeeManager.getByLoginToken(token);
        return ResourceUtility.buildOkResponse(employeeDto);
    }

    /**
     * Handles HTTP GET requests to retrieve all employees of the company associated with the logged user.
     * Validates the provided token and retrieves the list of EmployeeDto objects for the company.
     * Returns an HTTP OK response with the list of employees if successful.
     * If an exception occurs, throws a ValidationException with the error message.
     *
     * @param token the authorization token for validating the request.
     * @return a Response object containing the list of EmployeeDto objects or an error message.
     * @throws ValidationException if an error occurs during the process.
     */
    @GET
    @Path("/my-company")
    public Response getAllByLoggedUserCompany(@HeaderParam("Authorization") String token) {
        LoginManager.checkTokenValidation(token);
        List<EmployeeDto> dto = EmployeeManager.getAllByLoggedUserCompany(token);
        return ResourceUtility.buildOkResponse(dto);
    }

    /**
     * Handles HTTP POST requests to retrieve all employees of the specified company.
     * Consumes a JSON representation of a CompanyDto object.
     * Validates the provided authorization token and retrieves the list of EmployeeDto objects for the specified company.
     * Returns an HTTP OK response with the list of employees if successful.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token   the authorization token for validating the request.
     * @param company the CompanyDto object representing the company whose employees are to be retrieved.
     * @return a Response object containing the list of EmployeeDto objects or an error message.
     */
    @POST
    @Path("/companies")
    @Consumes("application/json")
    public Response getAllBySelectedCompany(@HeaderParam("Authorization") String token, CompanyDto company) {
        LoginManager.checkTokenValidation(token);
        List<EmployeeDto> newDto = EmployeeManager.getAllBySelectedCompany(token, company);
        return ResourceUtility.buildOkResponse(newDto);
    }

    @POST
    @Path("/message")
    @Consumes("application/json")
    public Response getByMessage(@HeaderParam("Authorization") String token, MessageDto message) {
        LoginManager.checkTokenValidation(token);
        EmployeeDto newDto = EmployeeManager.getByMessage(message);
        return ResourceUtility.buildOkResponse(newDto);
    }

    /**
     * Handles HTTP POST requests to create a new employee.
     * Consumes a JSON representation of an EmployeeToInsertDto object.
     * Validates the provided authorization token and creates the employee.
     * Returns an HTTP OK response with the updated list of EmployeeDto objects if successful.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token            the authorization token for validating the request.
     * @param employeeToInsert the EmployeeToInsertDto object containing the details of the employee to be created.
     * @return a Response object containing the updated list of EmployeeDto objects or an error message.
     */
    @POST
    @Path("/")
    @Consumes("application/json")
    public Response create(@HeaderParam("Authorization") String token, EmployeeToInsertDto employeeToInsert, CompanyDto company) {
        LoginManager.checkTokenValidation(token);
        List<EmployeeDto> newDto = EmployeeManager.create(token, employeeToInsert, company);
        return ResourceUtility.buildOkResponse(newDto);
    }

    /**
     * Handles HTTP PUT requests to modify an existing employee's details.
     * Consumes a JSON representation of an EmployeeDto object.
     * Validates the provided authorization token and updates the employee's details.
     * Returns an HTTP OK response with the updated list of EmployeeDto objects if successful.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token       the authorization token for validating the request.
     * @param employeeDto the EmployeeDto object containing the updated details of the employee.
     * @return a Response object containing the updated list of EmployeeDto objects or an error message.
     */
    @PUT
    @Path("/modify")
    @Consumes("application/json")
    public Response modifyEmployee(@HeaderParam("Authorization") String token, EmployeeDto employeeDto) {
        LoginManager.checkTokenValidation(token);
        List<EmployeeDto> newDto = EmployeeManager.modifyEmployee(employeeDto);
        return ResourceUtility.buildOkResponse(newDto);
    }

    /**
     * Handles HTTP PUT requests to modify the user's password.
     * Consumes a plain text representation of the new password.
     * This method is intended to change the default password during the user's first login.
     * Validates the provided authorization token and updates the password.
     * Returns an HTTP OK response with a boolean indicating the success of the operation.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token    the authorization token for validating the request.
     * @param password the new password to set for the user.
     * @return a Response object containing a boolean indicating success or an error message.
     */
    @PUT
    @Consumes("text/plain")
    @Path("/password")
    public Response modifyPassword(@HeaderParam("Authorization") String token, String password) {
        boolean res = EmployeeManager.modifyPassword(token, password);
        return ResourceUtility.buildOkResponse(res);
    }

    /**
     * Handles HTTP DELETE requests to remove specified employees.
     * Consumes a JSON representation of a list of EmployeeDto objects.
     * Validates the provided authorization token and deletes the specified employees.
     * Returns an HTTP OK response with the updated list of EmployeeDto objects if successful.
     * If an exception occurs, returns an HTTP Bad Request response with the error message.
     *
     * @param token             the authorization token for validating the request.
     * @param employeesToDelete the list of EmployeeDto objects representing the employees to be deleted.
     * @return a Response object containing the updated list of EmployeeDto objects or an error message.
     */
    @DELETE
    @Path("/")
    @Consumes("application/json")
    public Response remove(@HeaderParam("Authorization") String token, List<EmployeeDto> employeesToDelete) {
        LoginManager.checkTokenValidation(token);
        List<EmployeeDto> newDto = EmployeeManager.remove(employeesToDelete);
        return ResourceUtility.buildOkResponse(newDto);
    }
}
