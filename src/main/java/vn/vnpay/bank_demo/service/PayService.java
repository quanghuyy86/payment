package vn.vnpay.bank_demo.service;

import vn.vnpay.bank_demo.model.dto.payment.request.CheckSumRequestDTO;
import vn.vnpay.common.PaymentRequestDTO;

public interface PayService {
    void createPayment(PaymentRequestDTO paymentRequestDTO);

    String createCheckSum(CheckSumRequestDTO checkSumRequestDTO);
}
