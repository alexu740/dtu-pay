package com.dtu.pay.Service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

import com.dtu.pay.Model.User;
import com.dtu.pay.Model.Customer;
import com.dtu.pay.Model.Merchant;
import com.dtu.pay.Model.Payment;
import com.dtu.pay.Model.Dto.RegistrationDto;
import com.dtu.pay.Model.Dto.TokenRequestDto;
import com.dtu.pay.Service.SimpleDtuPay;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class MobileAppApiHelper {
    public String register(User usr, String bankAccountNumber) {
        RegistrationDto dto = new RegistrationDto();
        dto.setFirstName(usr.getFirstName());
        dto.setLastName(usr.getLastName());
        dto.setCpr(usr.getCpr());
        dto.setBankAccount(bankAccountNumber);
        if(usr instanceof Customer)
            return register(dto, "customers");
        else
            return register(dto, "merchants");
    }

    private String register(RegistrationDto payload, String entity) {
        String targetUrl = "http://" + "localhost" + ":8080/" + entity;
        Client client = ClientBuilder.newClient();
        Response response = client.target(targetUrl).request().post(Entity.entity(payload, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        } else {
            return "Failed to update customer: HTTP " + response.getStatus();
        }
    }

    public Customer getCustomer(String userId) {
        String targetUrl = "http://" + "localhost" + ":8080/customers/" + userId;
        Client client = ClientBuilder.newClient();
        
        Response response = client.target(targetUrl).request().get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Customer.class);
        } else {
            System.out.println("Failed to update customer: HTTP " + response.getStatus());
        }
        return null;
    }

    public String requestTokens(String userId, Integer numberOfTokens) {
        String targetUrl = "http://" + "localhost" + ":8080/tokens";
        Client client = ClientBuilder.newClient();

        var payload = new TokenRequestDto();
        payload.setUserId(userId);
        payload.setNumberOfTokens(numberOfTokens);

        Response response = client.target(targetUrl).request().post(Entity.entity(payload, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        } else {
            return "Failed to update customer: HTTP " + response.getStatus();
        }
    }
}
