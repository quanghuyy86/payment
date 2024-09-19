package vn.vnpay.bank_demo.controller.payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.bank_demo.common.dto.responses.BankResulMessage;
import vn.vnpay.bank_demo.model.dto.payment.request.CheckSumRequestDTO;
import vn.vnpay.bank_demo.model.dto.payment.request.PaymentRequestDTO;
import vn.vnpay.bank_demo.service.PayService;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {
    private final PayService payService;

    @PostMapping()
    public BankResulMessage restPayment(@RequestBody @Valid PaymentRequestDTO request) {
        payService.createPayment(request);
        return BankResulMessage.success();
    }

    @PostMapping("checksum")
    public ResponseEntity<String> createCheckSum(@RequestBody @Valid CheckSumRequestDTO request) {
        String checkSum = payService.createCheckSum(request);
        return ResponseEntity.status(HttpStatus.OK).body(checkSum);
    }
}
