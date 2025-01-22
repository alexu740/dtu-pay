package com.rest.start.Adapters;

import java.util.List;

import com.rest.start.Model.Dto.*;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import com.rest.start.Model.valueobjects.CustomerReport;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;


@ApplicationScoped
public class CustomerFacade {
    public CustomerFacade() {

    }

    public CustomerDto getCustomer(String id) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://customer-facade:8088/customers/" + id);
        Response response = target.request().get();
        return response.readEntity(CustomerDto.class);
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

    public String createTokens(String customerId, String tokenNumber) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://customer-facade:8088/customers/"+customerId+"/tokens/" + tokenNumber);
        Response response = target.request().post(Entity.entity("", MediaType.APPLICATION_JSON));
        return response.readEntity(String.class);
    }

    public List<CustomerReport> getReports(String customerId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://customer-facade:8088/customers/"+customerId+"/reports/");
        Response response = target.request().get();
        return response.readEntity(List.class);
    }
}
