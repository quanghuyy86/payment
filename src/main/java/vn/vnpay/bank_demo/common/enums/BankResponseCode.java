package vn.vnpay.bank_demo.common.enums;

import lombok.Getter;

@Getter
public enum BankResponseCode {
    SUCCESS("00", "Success"),
    FIELD_ERROR("01", "Field_error"),
    BANK_CODE_ERROR("02", "Bank code error"),
    CHECKSUM_ERROR("03", "CheckSum error"),
    SQL_ERROR("04", "SQL error"),
    BAD_GATEWAY_ERROR("99", "Internal server error");

    private final String code;
    private final String message;

    BankResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
