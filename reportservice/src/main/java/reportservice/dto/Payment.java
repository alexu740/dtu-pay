package reportservice.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {
    private String id;
    private String merchantId;
    private String token;
    private int amount;
    private String description;
}
