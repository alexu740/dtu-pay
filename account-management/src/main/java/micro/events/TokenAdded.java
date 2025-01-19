package micro.events;

import micro.aggregate.AccountId;

public class TokenAdded extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724483L;

	public String newToken;

    public TokenAdded(AccountId accountId, String newToken) {
        super("TokenAdded", new Object[] { accountId.getUuid(), newToken }, accountId);
        this.newToken = newToken;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
}
