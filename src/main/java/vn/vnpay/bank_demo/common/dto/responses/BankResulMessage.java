package vn.vnpay.bank_demo.common.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import vn.vnpay.bank_demo.common.enums.BankCode;
import vn.vnpay.bank_demo.common.enums.BankResponseCode;
import vn.vnpay.bank_demo.util.CheckSumUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BankResulMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    private String responseId;
    private String responseTime;
    private String checkSum;

    public BankResulMessage() {
    }

    public BankResulMessage(String code, String message) {
        this.code = code;
        this.message = message;
        this.responseId = generateResponseId();
        this.responseTime = getCurrentTime();
        this.checkSum = CheckSumUtil.calculateCheckSum(
                code + message + responseId + responseTime + BankCode.VNPAY.getBankCode());
    }

    public BankResulMessage(String code, String message, String responseId, String responseTime, String checkSum) {
        this.code = code;
        this.message = message;
        this.responseId = generateResponseId();
        this.responseTime = getCurrentTime();
        this.checkSum = CheckSumUtil.calculateCheckSum(
                code + message + responseId + responseTime + BankCode.VNPAY.getBankCode()
        );
    }

    public static BankResulMessage success() {
        String code = BankResponseCode.SUCCESS.getCode();
        String message = BankResponseCode.SUCCESS.getMessage();
        String responseId = generateResponseId();
        String responseTime = getCurrentTime();
        String checkSum = CheckSumUtil.calculateCheckSum(
                code + message + responseId + responseTime + BankCode.VNPAY.getBankCode()
        );
        return new BankResulMessage(code, message, responseId, responseTime, checkSum);
    }

    // Sinh UUID ngẫu nhiên cho responseId
    private static String generateResponseId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // Lấy thời gian hiện tại theo định dạng yyyyMMddHHmmss
    private static String getCurrentTime() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            return LocalDateTime.now().format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
