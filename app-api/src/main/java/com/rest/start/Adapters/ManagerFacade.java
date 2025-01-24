package com.rest.start.Adapters;

import java.util.List;

import com.rest.start.Model.valueobjects.ManagerReport;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
/**
 * @author: Xin Huang, s243442
 */
@ApplicationScoped
public class ManagerFacade {
    public ManagerFacade() {

    }
    
    public List<ManagerReport> getManagerReport() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://manager-facade:8090/payments");
        Response response = target.request().get();
        return response.readEntity(List.class);
    }
}
