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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class SimpleDtuPay {
    //public static String URL = "fm-06.compute.dtu.dk";
    public static String URL = "localhost";

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

    public boolean pay(int amount, String customerId, String merchantId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://" + SimpleDtuPay.URL + ":8080/payments");
        Payment payment = new Payment();
        payment.setCustomer(customerId);
        payment.setMerchant(merchantId);
        payment.setAmount(amount);

        Response response = target.request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));

        String pId = response.readEntity(String.class);
        client.close();
        return response.getStatus() == 200;
    }

    public Map<String, Payment> getListOfPayments() {
        try (Client client = ClientBuilder.newClient();){
            WebTarget target = client.target("http://" + SimpleDtuPay.URL + ":8080/payments");

            Response response = target.request().get();
            String jsonResponse = response.readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Payment> payments = mapper.readValue(jsonResponse, new TypeReference<Map<String, Payment>>() {});

            return payments;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean unregister(User usr, String id) {
        String entity = usr instanceof Customer ? "customers" : "merchants";
        String targetUrl = "http://" + SimpleDtuPay.URL + ":8080/" + entity + "/" + id;
        Client client = ClientBuilder.newClient();
        Response response = client.target(targetUrl).request().delete();
        return response.getStatus() == 200;
    }

    private String register(RegistrationDto payload, String entity) {
        String targetUrl = "http://" + SimpleDtuPay.URL + ":8080/" + entity;
        Client client = ClientBuilder.newClient();
        Response response = client.target(targetUrl).request().post(Entity.entity(payload, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        } else {
            return "Failed to update customer: HTTP " + response.getStatus();
        }
    }
}
