package com.rest.start;

import java.util.UUID;
import java.lang.IllegalArgumentException;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.rest.start.Adapters.CustomerFacade;
import com.rest.start.Adapters.MerchantFacade;
import com.rest.start.Model.DataStore;
import com.rest.start.Model.Payment;
import com.rest.start.Model.Dto.PaymentDto;
import com.rest.start.Service.PaymentService;

import dtu.ws.fastmoney.BankServiceException_Exception;

@Path("/payments")
public class PaymentResource {
    MerchantFacade merchantFacade;

    @Inject
    public PaymentResource(MerchantFacade merchantFacade) {
        this.merchantFacade = merchantFacade;
    }

    /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPayments(String paymentString) {
        return Response.status(Response.Status.OK).entity(payService.getAllPayments()).build();
    } */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processPayment(PaymentDto payment) {
        return Response.status(Response.Status.OK).entity(merchantFacade.initializePayment(payment)).build();
    }
}
