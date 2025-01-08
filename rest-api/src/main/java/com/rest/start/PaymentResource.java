package com.rest.start;

import java.util.UUID;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import com.rest.start.Model.DataStore;
import com.rest.start.Model.Payment;
import com.rest.start.Service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/payment")
public class PaymentResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPayments(String paymentString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String paymentJson = mapper.writeValueAsString(DataStore.payments);
    
            return Response.status(Response.Status.OK).entity(paymentJson).build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("Unexpected error")
            .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processPayment(String paymentString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Payment payment = mapper.readValue(paymentString, Payment.class);
            String custId = payment.getCustomer();
            String merchId = payment.getMerchant();
            
            if (!DataStore.customers.containsKey(custId)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("customer with id " + custId + " is unknown")
                    .build();
            }

            if (!DataStore.merchants.containsKey(merchId)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("merchant with id " + merchId + " is unknown")
                    .build();
            }

            PaymentService payService = new PaymentService();
            boolean result = payService.processPayment(custId, merchId, payment.getAmount());
            if(result) {
                String id = UUID.randomUUID().toString();
                DataStore.payments.put(id, payment);
                return Response.status(Response.Status.OK).entity(id).build();
            }
            else {
                return Response.status(Response.Status.FORBIDDEN)
                .entity("The payment is rejected. Please try again.")
                .build();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("Error: " + e.getMessage())
            .build();
        }
    }
}
