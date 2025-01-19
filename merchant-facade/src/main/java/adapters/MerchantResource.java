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
import boilerplate.implementations.RabbitMqQueue;
import service.MerchantFacadeService;
import jakarta.inject.Inject;
import dto.PaymentDto;
import dto.RegistrationDto;

@Path("/merchants")
public class MerchantResource {
	private MerchantFacadeService service = new MerchantRegistrationFactory().getService();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegistrationDto registrationRequest) {
        var result = service.create(registrationRequest);
        return Response.ok(result).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response unregister(@PathParam("id") String id) {
        //queue.publish(new Event("CustomerRegistrationRequested"))
        return Response.ok("merchant deleted").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/payments")
    public Response payment(@PathParam("id") String id, PaymentDto dto) {
        service.initialisePayment(dto);
        return Response.ok("merchant deleted").build();
    }
}