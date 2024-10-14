package vn.vnpay.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentConsumerResponse implements Serializable {
    private static final long serialVersionUID = 1111123L;
    private String code;
    private String message;
    private Object data;
}
