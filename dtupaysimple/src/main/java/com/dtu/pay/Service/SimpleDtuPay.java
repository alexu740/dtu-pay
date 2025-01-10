package com.dtu.pay.Service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

import com.dtu.pay.Model.Customer;
import com.dtu.pay.Model.Merchant;
import com.dtu.pay.Model.Payment;
import com.dtu.pay.Model.Dto.RegistrationDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class SimpleDtuPay {
    //public static String URL = "fm-06.compute.dtu.dk";
    public static String URL = "localhost";

    public String register(Customer cust, String bankAccountNumber) {
        try {
            RegistrationDto dto = new RegistrationDto();
            dto.setFirstName(cust.getFirstName());
            dto.setLastName(cust.getLastName());
            dto.setCpr(cust.getCpr());
            dto.setBankAccount(bankAccountNumber);
            return register(dto, "customers");
        } catch(Exception e) {
            e.printStackTrace();
            return "Could not create customer";
        }
    }

    public String register(Merchant merch, String bankAccountNumber) {
        try {
            RegistrationDto dto = new RegistrationDto();
            dto.setFirstName(merch.getFirstName());
            dto.setLastName(merch.getLastName());
            dto.setCpr(merch.getCpr());
            dto.setBankAccount(bankAccountNumber);
            return register(dto, "merchants");
        } catch(Exception e) {
            e.printStackTrace();
            return "Could not create merchant";
        }
    }

    public boolean pay(int amount, String customerId, String merchantId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://" + SimpleDtuPay.URL + ":8080/payments");
        try {
            Payment payment = new Payment();
            payment.setCustomer(customerId);
            payment.setMerchant(merchantId);
            payment.setAmount(amount);
    
            

            Response response = target.request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));

            String pId = response.readEntity(String.class);
            client.close();
            return response.getStatus() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            client.close();
        }
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

    public boolean unregister(String id, String entity) {
        String targetUrl = "http://" + SimpleDtuPay.URL + ":8080/" + entity + "/" + id;

        try (Client client = ClientBuilder.newClient();
            Response response = client.target(targetUrl).request().delete()) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String register(RegistrationDto payload, String entity) {
        String targetUrl = "http://" + SimpleDtuPay.URL + ":8080/" + entity;

        try (Client client = ClientBuilder.newClient();
            Response response = client.target(targetUrl).request().post(Entity.entity(payload, MediaType.APPLICATION_JSON))) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(String.class);
            } else {
                return "Failed to update customer: HTTP " + response.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
