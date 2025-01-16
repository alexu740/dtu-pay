package com.rest.start.facade;

import java.util.Map;

import com.dtu.pay.Service.SimpleDtuPay;

@ApplicationScoped
public class MerchantFacade {
    public MerchantFacade() {

    }

    public String createMerchant(RegistrationDto dto) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8089/customers");
        Response response = target.request().post(dto);
        return response.readEntity(String.class);
    }

    public String deleteMerchant(String id) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8089/customers");
        Response response = target.request().delete();
        return response.readEntity(String.class);
    }
}
