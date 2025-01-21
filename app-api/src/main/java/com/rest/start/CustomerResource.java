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
import com.rest.start.Adapters.CustomerFacade;

@Path("/customers")
public class CustomerResource {
    CustomerFacade customerHandler;

    @Inject
    public CustomerResource(CustomerFacade customerHandler) {
        this.customerHandler = customerHandler;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getCustomer(@PathParam("id") String id) {
        return Response.ok(customerHandler.getCustomer(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegistrationDto registrationRequest) {
        return Response.ok(customerHandler.createCustomer(registrationRequest)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response deregister(@PathParam("id") String id) {
        return Response.ok(customerHandler.deleteCustomer(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/tokens/{tokenNumber}")
    public Response createTokens(@PathParam("id") String customerId, @PathParam("tokenNumber") String tokenNumber) {
        return Response.ok(customerHandler.createTokens(customerId, tokenNumber)).build();
    }
}