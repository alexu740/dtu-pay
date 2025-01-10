package com.rest.start.Model;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rest.start.Model.Customer;
import com.rest.start.Model.Merchant;
import com.rest.start.Model.Payment;

@ApplicationScoped 
public class DataStore {
    public final Map<String, Customer> customers = new ConcurrentHashMap<>();
    public final Map<String, Merchant> merchants = new ConcurrentHashMap<>();
    public final Map<String, Payment> payments = new ConcurrentHashMap<>();

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public Map<String, Merchant> getMerchants() {
        return merchants;
    }

    public Map<String, Payment> getPayments() {
        return payments;
    }
}
