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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

import com.rest.start.Service.PaymentService;
import com.rest.start.Model.Merchant;
import com.rest.start.Model.Dto.RegistrationDto;

@Path("/merchants")
public class MerchantResource {
    PaymentService paymentService;

    @Inject
    public MerchantResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMerchants() {
        return Response.status(Response.Status.OK).entity(paymentService.getAllMerchants()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegistrationDto registrationRequest) {
        return Response.ok(paymentService.registerMerchant(registrationRequest)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response unregister(@PathParam("id") String id) {
        if (!paymentService.deleteMerchant(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("merchant not found")
                           .build();
        }
        return Response.ok("merchant deleted").build();
    }
}
