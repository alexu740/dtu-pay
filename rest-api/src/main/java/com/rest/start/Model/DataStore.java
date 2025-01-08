package com.rest.start.Model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rest.start.Model.Customer;
import com.rest.start.Model.Merchant;
import com.rest.start.Model.Payment;

public class DataStore {
    public static final Map<String, Customer> customers = new ConcurrentHashMap<>();
    public static final Map<String, Merchant> merchants = new ConcurrentHashMap<>();
    public static final Map<String, Payment> payments = new ConcurrentHashMap<>();
}
