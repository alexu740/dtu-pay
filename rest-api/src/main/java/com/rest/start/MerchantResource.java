package com.rest.start;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

import com.rest.start.Model.Customer;
import com.rest.start.Model.Merchant;
import com.rest.start.Model.Dto.RegistrationDto;
import com.rest.start.Model.DataStore;

@Path("/merchant")
public class MerchantResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMerchants() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String merchantJson = mapper.writeValueAsString(DataStore.merchants);
            return Response.status(Response.Status.OK).entity(merchantJson).build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("Unexpected error")
            .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RegistrationDto registrationRequest = mapper.readValue(payload, RegistrationDto.class);
    
            Merchant newMerchant = new Merchant(registrationRequest.getFirstName(), 
                                            registrationRequest.getLastName(),
                                            registrationRequest.getCpr(),
                                            registrationRequest.getBankAccount());
            UUID id = UUID.randomUUID();
            DataStore.merchants.put(id.toString(), newMerchant);
            return Response.ok(id.toString()).build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("Unexpected error")
            .build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/unregister/{id}")
    public Response unregister(String id) {
        if (!DataStore.merchants.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("customer with id " + id + " is unknown ")
                           .build();
        }
        DataStore.merchants.remove(id);
        return Response.ok(id + " deleted").build();
    }
}
