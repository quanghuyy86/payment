package vn.vnpay.bank_demo.common.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.vnpay.bank_demo.common.dto.responses.BankResulMessage;
import vn.vnpay.bank_demo.common.enums.BankResponseCode;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<BankResulMessage> handlingRuntimeException(RuntimeException exception) {
        BankResulMessage apiErrorResponse = new BankResulMessage();
        apiErrorResponse.setCode(BankResponseCode.BAD_GATEWAY_ERROR.getCode());
        apiErrorResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(apiErrorResponse);
    }

    @ExceptionHandler(value = BankException.class)
    ResponseEntity<BankResulMessage> handlingBankException(BankException exception) {
        BankResulMessage apiErrorResponse = new BankResulMessage();
        apiErrorResponse.setCode(BankResponseCode.BAD_GATEWAY_ERROR.getCode());
        apiErrorResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(apiErrorResponse);
    }

    @ExceptionHandler(value = CheckSumException.class)
    ResponseEntity<BankResulMessage> handlingCheckSumException(CheckSumException exception) {
        BankResulMessage apiErrorResponse = new BankResulMessage();
        apiErrorResponse.setCode(BankResponseCode.CHECKSUM_ERROR.getCode());
        apiErrorResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(apiErrorResponse);
    }

    @ExceptionHandler(value = BankCodeException.class)
    ResponseEntity<BankResulMessage> handlingBankCodeJwtException(BankCodeException exception) {
        BankResulMessage apiErrorResponse = new BankResulMessage();
        apiErrorResponse.setCode(BankResponseCode.BANK_CODE_ERROR.getCode());
        apiErrorResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(apiErrorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<BankResulMessage> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BankResulMessage apiErrorResponse = new BankResulMessage(BankResponseCode.FIELD_ERROR.getCode(),Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(apiErrorResponse);
    }

}
