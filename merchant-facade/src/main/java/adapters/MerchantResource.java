package adapters;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;

import boilerplate.implementations.RabbitMqQueue;
import service.MerchantFacadeService;
import jakarta.inject.Inject;
import dto.PaymentDto;
import dto.RegistrationDto;

/**
 * @author: Lukas Åkefeldt, s242204
 */
@Path("/merchants")
public class MerchantResource {
	private MerchantFacadeService service = new MerchantRegistrationFactory().getService();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Register new merchant", description = "Returns the merchant id")
    public Response register(RegistrationDto registrationRequest) {
        var result = service.create(registrationRequest);
        return Response.ok(result).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Operation(summary = "Deletes merchant", description = "Returns the status of the operation")
    public Response unregister(@PathParam("id") String id) {
        //queue.publish(new Event("CustomerRegistrationRequested"))
        return Response.ok(service.deregister(id)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/payments")
    @Operation(summary = "Starts a payment", description = "Returns the status of the operation")
    public Response payment(@PathParam("id") String id, PaymentDto dto) {
        var result = service.initialisePayment(dto);
        return Response.ok(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/reports")
    @Operation(summary = "Gets the merchant report", description = "Returns a list of payments for the merchant")
    public Response payment(@PathParam("id") String id) {
        var result = service.getReport(id);
        return Response.ok(result).build();
    }
}