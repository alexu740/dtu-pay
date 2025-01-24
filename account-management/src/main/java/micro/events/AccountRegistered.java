package micro.events;

import micro.aggregate.AccountId;
import micro.service.CorrelationId;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class AccountRegistered extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724482L;

    public String firstName;
    public String lastName;
    public String cpr;
    public String bankAccount;
    public boolean isCustomerAccountType; 

    public AccountRegistered(AccountId accountId, CorrelationId correlationId) {
        super("AccountRegistered", new Object[] { accountId.getUuid(), correlationId }, accountId);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public boolean isCustomerAccountType() {
        return isCustomerAccountType;
    }

    public void setCustomerAccountType(boolean isCustomerAccountType) {
        this.isCustomerAccountType = isCustomerAccountType;
    }
}
