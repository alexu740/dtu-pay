package com.rest.start.facade;

import java.util.Map;

import com.dtu.pay.Service.SimpleDtuPay;

@ApplicationScoped
public class CustomerFacade {
    public CustomerFacade() {

    }

    public String createCustomer(RegistrationDto dto) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8088/customers");
        Response response = target.request().post(dto);
        return response.readEntity(String.class);
    }

    public String deleteCustomer(String id) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8088/customers");
        Response response = target.request().delete();
        return response.readEntity(String.class);
    }
}
