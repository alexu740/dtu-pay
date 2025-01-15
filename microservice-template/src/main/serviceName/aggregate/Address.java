package usermanagement.domain.aggregate;

import java.io.Serializable;

import lombok.Value;

@Value
public class Address implements Serializable {
	private static final long serialVersionUID = 1239857120324729786L;
	private String city;
    private String state;
    private String postcode;
}
