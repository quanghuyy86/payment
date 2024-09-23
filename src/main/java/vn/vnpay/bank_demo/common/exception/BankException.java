package vn.vnpay.bank_demo.common.exception;

import lombok.Getter;
import vn.vnpay.bank_demo.common.enums.BankResponseCode;

@Getter
public class BankException extends RuntimeException {

    private final BankResponseCode responseCode;

    public BankResponseCode getResponseCode() {
        return responseCode;
    }

    public BankException(BankResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }


}
