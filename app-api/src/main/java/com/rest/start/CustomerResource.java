package com.rest.start;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;

import com.rest.start.Model.Dto.RegistrationDto;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.rest.start.Adapters.CustomerFacade;
/**
 * @author: Xin Huang, s243442
 */
@Path("/customers")
@Tag(name = "Customer Management", description = "Endpoints for managing customer accounts.")
public class CustomerResource {
    CustomerFacade customerHandler;

    @Inject
    public CustomerResource(CustomerFacade customerHandler) {
        this.customerHandler = customerHandler;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Operation(summary = "Get Customer", description = "Fetch details of a customer by their ID.")
    public Response getCustomer(@PathParam("id") String id) {
        return Response.ok(customerHandler.getCustomer(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/reports")
    @Operation(summary = "Get Customer Reports", description = "Retrieve all reports for a specific customer by their ID.")
    public Response getCustomerReports(@PathParam("id") String id) {
        return Response.ok(customerHandler.getReports(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Register Customer", description = "Register a new customer.")
    public Response register(RegistrationDto registrationRequest) {
        return Response.ok(customerHandler.createCustomer(registrationRequest)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Operation(summary = "Deregister Customer", description = "Delete a customer by their ID.")
    public Response deregister(@PathParam("id") String id) {
        return Response.ok(customerHandler.deleteCustomer(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/tokens/{tokenNumber}")
    @Operation(summary = "Create Tokens", description = "Generate tokens for a customer by their ID.")
    public Response createTokens(@PathParam("id") String customerId, @PathParam("tokenNumber") String tokenNumber) {
        return Response.ok(customerHandler.createTokens(customerId, tokenNumber)).build();
    }
}