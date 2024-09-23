package vn.vnpay.bank_demo.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.vnpay.bank_demo.common.enums.BankCode;
import vn.vnpay.bank_demo.common.exception.BankCodeException;
import vn.vnpay.bank_demo.common.exception.BankException;
import vn.vnpay.bank_demo.common.exception.CheckSumException;
import vn.vnpay.bank_demo.config.BankProperties;
import vn.vnpay.bank_demo.model.dto.payment.request.CheckSumRequestDTO;
import vn.vnpay.bank_demo.model.dto.payment.request.PaymentRequestDTO;
import vn.vnpay.bank_demo.model.entity.Payment;
import vn.vnpay.bank_demo.repository.PaymentRepository;
import vn.vnpay.bank_demo.service.PayService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static vn.vnpay.bank_demo.util.CheckSumUtil.calculateCheckSum;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayServiceImpl implements PayService {

    private final PaymentRepository paymentRepository;
    private final BankProperties bankProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void createPayment(PaymentRequestDTO request) {
        //Kiểm tra checkSum
        validateCheckSum(request);

        //kiểm tra bankcode
        validateBankCode(request);

        //Set Redis pool
        setRedis(request);

//        Payment payment = Payment.builder()
//                .tokenKey(request.getTokenKey())
//                .apiId(request.getApiId())
//                .mobile(request.getMobile())
//                .bankCode(request.getBankCode())
//                .accountNo(request.getAccountNo())
//                .payDate(request.getPayDate())
//                .additionalData(request.getAdditionalData())
//                .debitAmount(request.getDebitAmount())
//                .respDesc(request.getRespDesc())
//                .respCode(request.getRespCode())
//                .traceTransfer(request.getTraceTransfer())
//                .messageType(request.getMessageType())
//                .checkSum(request.getCheckSum())
//                .orderCode(request.getOrderCode())
//                .userName(request.getUserName())
//                .realAmount(request.getRealAmount())
//                .promotionCode(request.getPromotionCode())
//                .build();
//        paymentRepository.save(payment);

    }

    private void setRedis(PaymentRequestDTO request) {
        Gson gson = new Gson();
        String dataReq = gson.toJson(request);
        setDataToRedis(request.getBankCode(), request.getTokenKey(),dataReq);
    }


    private void validateBankCode(PaymentRequestDTO request) {
        boolean exist = false;
        for (BankProperties.BankConfig bankConfig : bankProperties.getBanks()) {
            if (bankConfig.getBankCode().equals(request.getBankCode())) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new BankCodeException("bankCode Failed");
        }
    }

    @Override
    public String createCheckSum(CheckSumRequestDTO request) {
        String input = request.getMobile() + request.getBankCode() + request.getAccountNo()
                + request.getPayDate() + request.getDebitAmount() + request.getRespCode()
                + request.getTraceTransfer() + request.getMessageType() + BankCode.VNPAY.getBankCode();
        return calculateCheckSum(input);
    }

    private static void validateCheckSum(PaymentRequestDTO request) {
        String input = request.getMobile() + request.getBankCode() + request.getAccountNo()
                + request.getPayDate() + request.getDebitAmount() + request.getRespCode()
                + request.getTraceTransfer() + request.getMessageType() + BankCode.VNPAY.getBankCode();

        String calculatedCheckSum = calculateCheckSum(input);

        if (!calculatedCheckSum.equals(request.getCheckSum())) {
            throw new CheckSumException("checkSum failed");
        }
    }
    // Hàm để set dữ liệu vào Redis
    //TODO: tìm hiểu try catch resource
    private void setDataToRedis(String bankCode, String tokenKey, String jsonData) {
        try {
            redisTemplate.opsForHash().put(bankCode, tokenKey, jsonData);
        } catch (Exception e) {
            throw new BankException(e.getMessage());
        }
    }

}
