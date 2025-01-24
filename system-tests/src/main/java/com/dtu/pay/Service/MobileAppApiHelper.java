package com.dtu.pay.Service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import com.dtu.pay.Model.User;

import java.util.ArrayList;
import java.util.List;

import com.dtu.pay.Model.Customer;
import com.dtu.pay.Model.Dto.CustomerTokensResponseDto;
import com.dtu.pay.Model.Dto.PaymentDto;
import com.dtu.pay.Model.Dto.RegistrationDto;
import com.dtu.pay.Model.Dto.TokenRequestDto;
import com.dtu.pay.Model.Dto.ManagerReport;
import com.dtu.pay.Model.Dto.CustomerReport;
import com.dtu.pay.Model.Dto.MerchantReport;

/**
 * @author: Alex Ungureanu (s225525)
 */

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

    public CustomerTokensResponseDto getCustomer(String userId) {
        String targetUrl = "http://" + "localhost" + ":8080/customers/" + userId;
        Client client = ClientBuilder.newClient();
        
        Response response = client.target(targetUrl).request().get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(CustomerTokensResponseDto.class);
        } else {
            System.out.println("Failed to update customer: HTTP " + response.getStatus());
        }
        return null;
    }

    public String requestTokens(String userId, Integer numberOfTokens) {
        String targetUrl = "http://" + "localhost" + ":8080/customers/"+userId + "/tokens/" +numberOfTokens;
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

    public String pay(String customerId, String merchantId, String token, int amount) {
        String targetUrl = "http://" + "localhost" + ":8080/payments";
        Client client = ClientBuilder.newClient();

        var payload = new PaymentDto();
        payload.setAmount(amount);
        payload.setCustomerId(customerId);
        payload.setMerchantId(merchantId);
        payload.setToken(token);

        Response response = client.target(targetUrl).request().post(Entity.entity(payload, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        } else {
            return "Failed to update customer: HTTP " + response.getStatus();
        }
    }

    public List<CustomerReport> getCustomerReport(String customerId) {
        String targetUrl = "http://" + "localhost" + ":8080/customers/" + customerId + "/reports";
        Client client = ClientBuilder.newClient();

        Response response = client.target(targetUrl).request().get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<List<CustomerReport>> genericType = new GenericType<List<CustomerReport>>() {};
            return response.readEntity(genericType);
        } else {
            return new ArrayList<>();
        }
    }

    public List<MerchantReport> getMerchantReport(String customerId) {
        String targetUrl = "http://" + "localhost" + ":8080/merchants/" + customerId + "/reports";
        Client client = ClientBuilder.newClient();

        Response response = client.target(targetUrl).request().get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<List<MerchantReport>> genericType = new GenericType<List<MerchantReport>>() {};
            return response.readEntity(genericType);
        } else {
            return new ArrayList<>();
        }
    }

    public List<ManagerReport> getManagerReport() {
        String targetUrl = "http://" + "localhost" + ":8080/payments";
        Client client = ClientBuilder.newClient();

        Response response = client.target(targetUrl).request().get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<List<ManagerReport>> genericType = new GenericType<List<ManagerReport>>() {};
            return response.readEntity(genericType);
        } else {
            return new ArrayList<>();
        }
    }
}
