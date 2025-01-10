package com.rest.start;

import java.util.UUID;
import java.lang.IllegalArgumentException;
import dtu.ws.fastmoney.BankServiceException_Exception;
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

@Path("/payments")
public class PaymentResource {
    PaymentService payService = new PaymentService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPayments(String paymentString) {
        return Response.status(Response.Status.OK).entity(DataStore.payments).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processPayment(Payment payment) {
        try {
            String id = payService.processPayment(payment);
            return Response.status(Response.Status.OK).entity(id).build();
        } catch(BankServiceException_Exception e1) {
            return Response.status(Response.Status.FORBIDDEN)
            .entity("The payment is rejected. Please try again.")
            .build();
        } catch(IllegalArgumentException e2) {
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("customer or merchant is unknown")
            .build();
        }
    }
}
