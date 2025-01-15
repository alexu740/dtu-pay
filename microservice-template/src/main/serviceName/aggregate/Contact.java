package usermanagement.domain.aggregate;

import java.io.Serializable;

import lombok.Value;

@Value
public class Contact implements Serializable {
	private static final long serialVersionUID = -2454254318796957308L;
	private String type;
    private String detail;
}
