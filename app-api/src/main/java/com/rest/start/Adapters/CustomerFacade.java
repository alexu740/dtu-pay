package com.rest.start.Adapters;

import com.rest.start.Model.Dto.*;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;


@ApplicationScoped
public class CustomerFacade {
    public CustomerFacade() {

    }

    public String createCustomer(RegistrationDto dto) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://customer-facade:8088/customers");
        Response response = target.request().post(Entity.entity(dto, MediaType.APPLICATION_JSON));
        return response.readEntity(String.class);
    }

    public String deleteCustomer(String id) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://customer-facade:8088/customers/"+id);
        Response response = target.request().delete();
        return response.readEntity(String.class);
    }
}
