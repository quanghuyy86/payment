package vn.vnpay.bank_demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import vn.vnpay.bank_demo.common.enums.BankCode;
import vn.vnpay.bank_demo.common.exception.BankCodeException;
import vn.vnpay.bank_demo.common.exception.CheckSumException;
import vn.vnpay.bank_demo.config.BankProperties;
import vn.vnpay.bank_demo.model.dto.payment.request.CheckSumRequestDTO;
import vn.vnpay.bank_demo.model.dto.payment.request.PaymentRequestDTO;
import vn.vnpay.bank_demo.model.entity.Payment;
import vn.vnpay.bank_demo.repository.PaymentRepository;
import vn.vnpay.bank_demo.service.PayService;

import java.util.HashMap;
import java.util.Map;

import static vn.vnpay.bank_demo.util.CheckSumUtil.calculateCheckSum;

@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final PaymentRepository paymentRepository;
    private final BankProperties bankProperties;
    private final JedisPool jedisPool;

    @Override
    public void createPayment(PaymentRequestDTO request) {
        //Kiểm tra checkSum
        validateCheckSum(request);

        //kiểm tra bankcode
        validateBankCode(request);

        //Set Redis pool
        setRedis(request);

        Payment payment = Payment.builder()
                .tokenKey(request.getTokenKey())
                .apiId(request.getApiId())
                .mobile(request.getMobile())
                .bankCode(request.getBankCode())
                .accountNo(request.getAccountNo())
                .payDate(request.getPayDate())
                .additionalData(request.getAdditionalData())
                .debitAmount(request.getDebitAmount())
                .respDesc(request.getRespDesc())
                .respCode(request.getRespCode())
                .traceTransfer(request.getTraceTransfer())
                .messageType(request.getMessageType())
                .checkSum(request.getCheckSum())
                .orderCode(request.getOrderCode())
                .userName(request.getUserName())
                .realAmount(request.getRealAmount())
                .promotionCode(request.getPromotionCode())
                .build();
        paymentRepository.save(payment);

    }

    private void setRedis(PaymentRequestDTO request) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("tokenKey", request.getTokenKey());
        dataMap.put("apiId", request.getApiId());
        dataMap.put("mobile", request.getMobile());
        dataMap.put("bankCode", request.getBankCode());
        dataMap.put("accountNo", request.getAccountNo());
        dataMap.put("payDate", String.valueOf(request.getPayDate()));
        dataMap.put("additionalData", request.getAdditionalData());
        dataMap.put("debitAmount", String.valueOf(request.getDebitAmount()));
        dataMap.put("respCode", request.getRespCode());
        dataMap.put("respDesc", request.getRespDesc());
        dataMap.put("traceTransfer", request.getTraceTransfer());
        dataMap.put("messageType", request.getMessageType());
        dataMap.put("checkSum", request.getCheckSum());
        dataMap.put("orderCode", request.getOrderCode());
        dataMap.put("userName", request.getUserName());
        dataMap.put("realAmount", String.valueOf(request.getRealAmount()));
        dataMap.put("promotionCode", request.getPromotionCode());

        setDataToRedis(request.getBankCode(), request.getTokenKey(),dataMap);
    }

//    private void validateBankCode(PaymentRequestDTO request) {
//        boolean exist = bankProperties.getBanks().stream()
//                .anyMatch(bankConfig -> bankConfig.getBankCode().equals(request.getBankCode()));
//        if (!exist) {
//            throw new BankCodeException("bankCode Failed");
//        }
//    }

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
    private void setDataToRedis(String bankCode, String tokenKey, Map<String, String> jsonData) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(bankCode, tokenKey, jsonData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
