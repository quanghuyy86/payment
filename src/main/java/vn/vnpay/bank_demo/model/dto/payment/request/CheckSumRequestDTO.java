package vn.vnpay.bank_demo.model.dto.payment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckSumRequestDTO {
    @NotBlank(message = "Field not null")
    private String mobile;
    @NotBlank(message = "Field not null")
    private String bankCode;
    @NotBlank(message = "Field not null")
    private String privateKey;
    @NotBlank(message = "Field not null")
    private String accountNo;
    @NotBlank(message = "Field not null")
    private String payDate;
    @NotBlank(message = "Field not null")
    private String debitAmount;
    @NotBlank(message = "Field not null")
    private String respCode;
    @NotBlank(message = "Field not null")
    private String traceTransfer;
    @NotBlank(message = "Field not null")
    private String messageType;
}
