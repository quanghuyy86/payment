package vn.vnpay.bank_demo.model.dto.auth.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResDto {
    private boolean authenticated;
    private String token;
}
