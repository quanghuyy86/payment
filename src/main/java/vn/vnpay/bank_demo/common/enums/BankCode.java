package vn.vnpay.bank_demo.common.enums;

import lombok.Getter;

@Getter
public enum BankCode {
    VNPAY("VNPAY", "ghffffffffff");

    private final String bankCode;
    private final String privateKey;

    BankCode(String bankCode, String privateKey) {
        this.bankCode = bankCode;
        this.privateKey = privateKey;
    }
}
