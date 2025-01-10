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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

import com.rest.start.Service.PaymentService;
import com.rest.start.Model.Merchant;
import com.rest.start.Model.Dto.RegistrationDto;
import com.rest.start.Model.DataStore;

@Path("/merchants")
public class MerchantResource {
    PaymentService paymentService = new PaymentService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMerchants() {
        return Response.status(Response.Status.OK).entity(paymentService.getAllMerchants()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegistrationDto registrationRequest) {
        String merchantId = paymentService.registerMerchant(registrationRequest);
        return Response.ok(merchantId).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response unregister(@PathParam("id") String id) {
        boolean result = paymentService.deleteMerchant(id);
        if (!result) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("merchant not found")
                           .build();
        }
        return Response.ok("merchant deleted").build();
    }
}
