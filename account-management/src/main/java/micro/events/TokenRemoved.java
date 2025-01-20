package micro.events;

import micro.aggregate.AccountId;

public class TokenRemoved extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724483L;

	public String removedToken;

    public TokenRemoved(AccountId accountId, String removedToken) {
        super("TokenRemoved", new Object[] {}, accountId);
        this.removedToken = removedToken;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getToken() {
        return removedToken;
    }

    public void setToken(String removedToken) {
        this.removedToken = removedToken;
    }
}
