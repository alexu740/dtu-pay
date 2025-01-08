package com.rest.start.Service;

import com.rest.start.Model.Merchant;

import java.math.BigDecimal;

import com.rest.start.Model.Customer;
import com.rest.start.Model.DataStore;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankServiceService;

public class PaymentService {
    public boolean processPayment(String customerId, String merchantId, int amount) {
        Customer cust = DataStore.customers.get(customerId);
        Merchant merch = DataStore.merchants.get(merchantId);

        try {
            callBank(cust, merch, amount);
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void callBank(Customer cust, Merchant merch, int amount) throws BankServiceException_Exception {
        BankService bank = new BankServiceService().getBankServicePort();
        bank.transferMoneyFromTo(cust.getBankAccount(), merch.getBankAccount(), new BigDecimal(amount), "From " + cust.getCpr() + " to " + merch.getCpr());
    }
 }
