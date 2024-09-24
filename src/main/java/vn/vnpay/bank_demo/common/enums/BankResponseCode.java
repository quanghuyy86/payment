package vn.vnpay.bank_demo.common.enums;

import lombok.Getter;

@Getter
public enum BankResponseCode {
    SUCCESS("00", "Success"),
    FIELD_ERROR("01", "Field_error"),
    BANK_CODE_ERROR("02", "Bank code error"),
    CHECKSUM_ERROR("03", "CheckSum error"),
    LOST_CONNECTION_TO_REDIS("04", "Lost connection to redis"),
    SQL_ERROR("05", "SQL error"),
    BAD_GATEWAY_ERROR("99", "Internal server error");

    private final String code;
    private final String message;

    BankResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
