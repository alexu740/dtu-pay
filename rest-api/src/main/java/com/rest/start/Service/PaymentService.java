package com.rest.start.Service;

import dtu.ws.fastmoney.BankServiceException_Exception;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.lang.IllegalArgumentException;

import com.rest.start.Model.Customer;
import com.rest.start.Model.Merchant;
import com.rest.start.Model.Payment;
import com.rest.start.Model.DataStore;
import com.rest.start.Model.Dto.RegistrationDto;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankServiceService;

public class PaymentService {
    public String processPayment(Payment payment) throws BankServiceException_Exception, IllegalArgumentException  {
        if(payment.getCustomer() == null || 
        payment.getMerchant() == null ||
        !DataStore.customers.containsKey(payment.getCustomer()) || 
        !DataStore.merchants.containsKey(payment.getMerchant())) {
            throw new IllegalArgumentException("Validation failed");
        }

        Customer cust = DataStore.customers.get(payment.getCustomer());
        Merchant merch = DataStore.merchants.get(payment.getMerchant());

        callBank(cust, merch, payment.getAmount());

        String id = UUID.randomUUID().toString();
        DataStore.payments.put(id, payment);
        return id;
    }

    public String registerCustomer(RegistrationDto dto) {
        Customer newCustomer = new Customer(dto.getFirstName(), 
        dto.getLastName(),
        dto.getCpr(),
        dto.getBankAccount());
        String id = UUID.randomUUID().toString();
        DataStore.customers.put(id, newCustomer);
        return id;
    }

    public Map<String, Customer> getAllCustomers() {
        return DataStore.customers;
    }

    public boolean deteleCustomer(String customerId) {
        if(!DataStore.customers.containsKey(customerId)) {
            return false;
        }
        DataStore.customers.remove(customerId);
        return true;
    }

    public String registerMerchant(RegistrationDto dto) {
        Merchant newMerchant = new Merchant(dto.getFirstName(), 
        dto.getLastName(),
        dto.getCpr(),
        dto.getBankAccount());
        String id = UUID.randomUUID().toString();
        DataStore.merchants.put(id, newMerchant);
        return id;
    }

    public Map<String, Merchant> getAllMerchants() {
        return DataStore.merchants;
    }

    public boolean deleteMerchant(String merchantId) {
        if(!DataStore.merchants.containsKey(merchantId)) {
            return false;
        }
        DataStore.customers.remove(merchantId);
        return true;
    }

    private void callBank(Customer cust, Merchant merch, int amount) throws BankServiceException_Exception {
        BankService bank = new BankServiceService().getBankServicePort();
        bank.transferMoneyFromTo(cust.getBankAccount(), merch.getBankAccount(), new BigDecimal(amount), "From " + cust.getCpr() + " to " + merch.getCpr());
    }
 }
