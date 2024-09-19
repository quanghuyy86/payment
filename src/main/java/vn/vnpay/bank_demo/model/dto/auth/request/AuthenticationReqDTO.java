package vn.vnpay.bank_demo.model.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationReqDTO {
    @NotBlank(message = "Tên đăng nhập không được có khoảng trắng")
    private String userName;
    @NotEmpty(message = "Mật khẩu trống")
    private String password;
}
