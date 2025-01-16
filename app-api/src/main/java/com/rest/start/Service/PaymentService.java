package com.rest.start.Service;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

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
import dtu.ws.fastmoney.BankServiceException_Exception;

@ApplicationScoped
public class PaymentService {
    private DataStore dataStore;

    @Inject
    public PaymentService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public String processPayment(Payment payment) throws BankServiceException_Exception, IllegalArgumentException  {
        if(payment.getCustomer() == null || 
        payment.getMerchant() == null ||
        !dataStore.getCustomers().containsKey(payment.getCustomer()) || 
        !dataStore.getMerchants().containsKey(payment.getMerchant())) {
            throw new IllegalArgumentException("Validation failed");
        }

        Customer cust = dataStore.getCustomers().get(payment.getCustomer());
        Merchant merch = dataStore.getMerchants().get(payment.getMerchant());

        callBank(cust, merch, payment.getAmount());

        String id = UUID.randomUUID().toString();
        dataStore.getPayments().put(id, payment);
        return id;
    }

    public Map<String, Payment> getAllPayments() {
        return dataStore.getPayments();
    }

    public String registerCustomer(RegistrationDto dto) {
        Customer newCustomer = new Customer(dto.getFirstName(), 
        dto.getLastName(),
        dto.getCpr(),
        dto.getBankAccount());
        String id = UUID.randomUUID().toString();
        dataStore.getCustomers().put(id, newCustomer);
        return id;
    }

    public Map<String, Customer> getAllCustomers() {
        return dataStore.getCustomers();
    }

    public boolean deteleCustomer(String customerId) {
        return dataStore.getCustomers().remove(customerId) != null;
    }

    public String registerMerchant(RegistrationDto dto) {
        Merchant newMerchant = new Merchant(dto.getFirstName(), 
        dto.getLastName(),
        dto.getCpr(),
        dto.getBankAccount());
        String id = UUID.randomUUID().toString();
        dataStore.getMerchants().put(id, newMerchant);
        return id;
    }

    public Map<String, Merchant> getAllMerchants() {
        return dataStore.getMerchants();
    }

    public boolean deleteMerchant(String merchantId) {
        return dataStore.getMerchants().remove(merchantId) != null;
    }

    private void callBank(Customer cust, Merchant merch, int amount) throws BankServiceException_Exception {
        BankService bank = new BankServiceService().getBankServicePort();
        bank.transferMoneyFromTo(cust.getBankAccount(), merch.getBankAccount(), new BigDecimal(amount), "From " + cust.getCpr() + " to " + merch.getCpr());
    }
 }
