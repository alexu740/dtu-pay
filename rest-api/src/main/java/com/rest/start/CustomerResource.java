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
import com.rest.start.Service.PaymentService;
import com.rest.start.Model.Customer;

@Path("/customers")
public class CustomerResource {
    PaymentService paymentService;

    @Inject
    public CustomerResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCustomers() {
        return Response.status(Response.Status.OK).entity(paymentService.getAllCustomers()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegistrationDto registrationRequest) {
        return Response.ok(paymentService.registerCustomer(registrationRequest)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response unregister(@PathParam("id") String id) {
        if (!paymentService.deteleCustomer(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("customer not found")
                           .build();
        }
        return Response.ok("customer deleted").build();
    }
}