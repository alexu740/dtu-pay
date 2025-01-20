package micro.adapters;

public interface ExternalBank {
    public boolean pay(String fromBankAccount, String toBankAccount, int amount, String paymentNote);
}
