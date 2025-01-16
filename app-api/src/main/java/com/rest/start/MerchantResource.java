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
import com.rest.start.Adapters.MerchantFacade;

@Path("/merchants")
public class MerchantResource {
    MerchantFacade merchantHandler;

    @Inject
    public MerchantResource(MerchantFacade merchantHandler) {
        this.merchantHandler = merchantHandler;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegistrationDto registrationRequest) {
        return Response.ok(merchantHandler.createMerchant(registrationRequest)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response unregister(@PathParam("id") String id) {
        return Response.ok(merchantHandler.deleteMerchant(id)).build();
    }
}
