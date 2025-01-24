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
import service.CustomerFacadeService;

import org.eclipse.microprofile.openapi.annotations.Operation;

import dto.RegistrationDto;
/**
 * @author: Alex Ungureanu (s225525)
 */
@Path("/customers")
public class CustomerResource {
	private CustomerFacadeService service = new CustomerRegistrationFactory().getService();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Register new customer", description = "Returns the customer id")
    public Response register(RegistrationDto registrationRequest) {
        var result = service.create(registrationRequest);
        return Response.ok(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Operation(summary = "Get the customer by id", description = "Returns the customer by id, containing the tokens")
    public Response getCustomer(@PathParam("id") String id) {
        //queue.publish(new Event("CustomerRegistrationRequested"))
        return Response.ok(service.get(id)).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/tokens/{tokenNumber}")
    @Operation(summary = "Creates tokens", description = "Returns success/failed message regarding the token registration process")
    public Response createTokens(@PathParam("id") String customerId, @PathParam("tokenNumber") String tokenNumber) {
        return Response.ok(service.createTokens(customerId, tokenNumber)).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/reports")
    @Operation(summary = "Gets the payments for the customer", description = "Returns a list of payments")
    public Response payment(@PathParam("id") String id) {
        var result = service.getReport(id);
        return Response.ok(result).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Operation(summary = "Deletes the customer", description = "Returns a status of the process")
    public Response deregister(@PathParam("id") String id) {
        //queue.publish(new Event("CustomerRegistrationRequested"))
        var result = service.deregister(id);
        return Response.ok("customer deleted").build();
    }
}