package micro.adapters;

import java.math.BigDecimal;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class FastMoneyBank implements ExternalBank {
    @Override
    public boolean pay(String fromBankAccount, String toBankAccount, int amount, String paymentNote)  {
        try {
            BankService bank = new BankServiceService().getBankServicePort();
            bank.transferMoneyFromTo(fromBankAccount, toBankAccount, new BigDecimal(amount), paymentNote);
            return true;
        }
        catch(BankServiceException_Exception ex) {
            System.out.println("Payment rejected by bank: " + ex.getMessage());
            return false;
        }
    }
}
