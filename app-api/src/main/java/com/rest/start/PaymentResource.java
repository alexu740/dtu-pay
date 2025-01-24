package com.rest.start;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;

import com.rest.start.Adapters.ManagerFacade;
import com.rest.start.Adapters.MerchantFacade;
import com.rest.start.Model.Dto.PaymentDto;
/**
 * @author: Xin Huang, s243442
 */
@Path("/payments")
public class PaymentResource {
    MerchantFacade merchantFacade;
    ManagerFacade managerFacade;

    @Inject
    public PaymentResource(MerchantFacade merchantFacade, ManagerFacade managerFacade) {
        this.merchantFacade = merchantFacade;
        this.managerFacade = managerFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all payments", description = "Get manager report of all payments")
    public Response listPayments(String paymentString) {
        return Response.status(Response.Status.OK).entity(managerFacade.getManagerReport()).build();
    } 

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Start payment", description = "Start the payment process")
    public Response processPayment(PaymentDto payment) {
        return Response.status(Response.Status.OK).entity(merchantFacade.initializePayment(payment)).build();
    }
}
