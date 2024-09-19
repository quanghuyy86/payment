package vn.vnpay.bank_demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token_key", columnDefinition = "VARCHAR(MAX)")
    private String tokenKey;

    @Column(name = "api_id", length = 255)
    private String apiId;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "bank_code", length = 20)
    private String bankCode;

    @Column(name = "account_no", length = 50)
    private String accountNo;

    @Column(name = "pay_date")
    private LocalDateTime payDate;

    @Column(name = "additional_data", columnDefinition = "VARCHAR(MAX)")
    private String additionalData;

    @Column(name = "debit_amount", precision = 18, scale = 2)
    private BigDecimal debitAmount;

    @Column(name = "resp_code", length = 10)
    private String respCode;

    @Column(name = "resp_desc", length = 255)
    private String respDesc;

    @Column(name = "trace_transfer", length = 50)
    private String traceTransfer;

    @Column(name = "message_type", length = 50)
    private String messageType;

    @Column(name = "check_sum", length = 255)
    private String checkSum;

    @Column(name = "order_code", length = 50)
    private String orderCode;

    @Column(name = "user_name", length = 255)
    private String userName;

    @Column(name = "real_amount", precision = 18, scale = 2)
    private BigDecimal realAmount;

    @Column(name = "promotion_code", length = 50)
    private String promotionCode;
}
