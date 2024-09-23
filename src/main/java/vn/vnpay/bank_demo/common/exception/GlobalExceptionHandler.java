package vn.vnpay.bank_demo.common.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.vnpay.bank_demo.common.dto.responses.BankResulMessage;
import vn.vnpay.bank_demo.common.enums.BankResponseCode;

import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<BankResulMessage> handlingException(Exception exception) {
        if (exception instanceof BankException) {
            BankResulMessage response = BankResulMessage.builder()
                    .code(((BankException) exception).getResponseCode().getCode())
                    .message(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } else if (exception instanceof SQLException) {
            BankResulMessage response = BankResulMessage.builder()
                    .code(BankResponseCode.SQL_ERROR.getCode())
                    .message(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) exception;
            BindingResult bindingResult = ex.getBindingResult();

            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            BankResulMessage response = BankResulMessage.builder()
                    .code(BankResponseCode.FIELD_ERROR.getCode())
                    .message(errorMessages)
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } else {
            BankResulMessage response = BankResulMessage.builder()
                    .code(BankResponseCode.BAD_GATEWAY_ERROR.getCode())
                    .message(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
    }


}
