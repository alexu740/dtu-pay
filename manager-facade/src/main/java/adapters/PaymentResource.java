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
import service.ManagerFacadeService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.openapi.annotations.Operation;
/**
 * @author: Alex Ungureanu (s225525)
 */
@Path("/payments")
public class PaymentResource {
	private ManagerFacadeService service = new ManagerRegistrationFactory().getService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Gets manager report", description = "Returns the list of all payments")
    public Response payment() {
        var result = service.getReport();
        return Response.ok(result).build();
    }
}