package vn.vnpay.bank_demo.model.dto.payment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    @NotBlank(message = "Field tokenKey cannot be null, empty, or blank")
    private String tokenKey;

    @NotBlank(message = "Field apiId cannot be null, empty")
    private String apiId;

    @NotBlank(message = "Field mobile cannot be null, empty, or blank")
    private String mobile;

    @NotBlank(message = "Field bankCode cannot be null, empty, or blank")
    private String bankCode;

    @NotBlank(message = "Field privateKey cannot be null, empty, or blank")
    private String privateKey;

    @NotBlank(message = "Field accountNo cannot be null, empty, or blank")
    private String accountNo;

    @NotNull(message = "Pay date cannot be null")
    @Pattern(regexp = "^\\d{14}$", message = "Pay date must be in the format yyyyMMddHHmmss")
    private String payDate;

    @NotBlank(message = "Field additionalData cannot be null, empty, or blank")
    private String additionalData;

    @NotNull(message = "debitAmount cannot be null")
    private BigDecimal debitAmount;

    @NotBlank(message = "Field respCode cannot be null, empty, or blank")
    private String respCode;

    @NotBlank(message = "Field respDesc cannot be null, empty, or blank")
    private String respDesc;

    @NotBlank(message = "Field traceTransfer cannot be null, empty, or blank")
    private String traceTransfer;

    @NotBlank(message = "Field messageType cannot be null, empty, or blank")
    private String messageType;

    @NotBlank(message = "Field checkSum cannot be null, empty, or blank")
    private String checkSum;

    @NotBlank(message = "Field orderCode cannot be null, empty, or blank")
    private String orderCode;

    @NotBlank(message = "Field userName cannot be null, empty, or blank")
    private String userName;

    @NotNull(message = "realAmount cannot be null")
    private BigDecimal realAmount;

    private String promotionCode;

    private String addValue = "{\"payMethod\":\"01\",\"payMethodMMS\":1}";

}
