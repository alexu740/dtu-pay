package micro.adapters;
/**
 * @author: Alex Ungureanu (s225525)
 */
public interface ExternalBank {
    public boolean pay(String fromBankAccount, String toBankAccount, int amount, String paymentNote);
}
