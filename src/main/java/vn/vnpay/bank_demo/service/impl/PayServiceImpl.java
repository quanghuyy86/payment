package vn.vnpay.bank_demo.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.vnpay.bank_demo.common.enums.BankResponseCode;
import vn.vnpay.bank_demo.common.exception.BankException;
import vn.vnpay.bank_demo.config.bank.Banks;
import vn.vnpay.bank_demo.model.dto.payment.request.CheckSumRequestDTO;
import vn.vnpay.bank_demo.model.dto.payment.request.PaymentRequestDTO;
import vn.vnpay.bank_demo.repository.PaymentRepository;
import vn.vnpay.bank_demo.service.PayService;

import static vn.vnpay.bank_demo.util.CheckSumUtil.calculateCheckSum;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayServiceImpl implements PayService {

    private final PaymentRepository paymentRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Banks banks;
    private final Gson gson;

    @Override
    public void createPayment(PaymentRequestDTO request) {
        log.info("Begin create-payment: {}", gson.toJson(request));

        //Check bankCode
        validateBankCode(request.getBankCode());

        //Check privateKey
        checkPrivateKey(request.getBankCode(), request.getPrivateKey());

        //Kiểm tra checkSum
        validateCheckSum(request);

        //Set Redis pool
        setRedis(request);
        log.info("End create-payment: Success");

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
        log.info("Start set data to redis");
        String dataReq = gson.toJson(request);
        setDataToRedis(request.getBankCode(), request.getTokenKey(), dataReq);
        log.info("End set data to redis: Success");
    }


    private void validateBankCode(String bankCode) {
        log.info("Start validate BankCode");
        boolean exist = banks.getBankList().stream()
                .anyMatch(bank -> bank.getBankCode().equals(bankCode));
        if (!exist) {
            log.info("Error code 02: BankCode Failed");
            throw new BankException(BankResponseCode.BANK_CODE_ERROR, "bankCode Failed");
        }
        log.info("End validate BankCode: Success");
    }

    @Override
    public String createCheckSum(CheckSumRequestDTO request) {
        log.info("Begin create-checkSum: {}", gson.toJson(request));

        validateBankCode(request.getBankCode());

        checkPrivateKey(request.getBankCode(), request.getPrivateKey());

        String input = request.getMobile() + request.getBankCode() + request.getAccountNo()
                + request.getPayDate() + request.getDebitAmount() + request.getRespCode()
                + request.getTraceTransfer() + request.getMessageType() + request.getPrivateKey();
        String checkSum = calculateCheckSum(input);
        log.info("End create-checkSum: {}", checkSum);
        return checkSum;
    }

    private static void validateCheckSum(PaymentRequestDTO request) {
        log.info("Start validate checksum");
        String input = request.getMobile() + request.getBankCode() + request.getAccountNo()
                + request.getPayDate() + request.getDebitAmount() + request.getRespCode()
                + request.getTraceTransfer() + request.getMessageType() + request.getPrivateKey();

        String calculatedCheckSum = calculateCheckSum(input);
        log.info("Checksum request: " + request.getCheckSum());
        if (!calculatedCheckSum.equals(request.getCheckSum())) {
            log.warn("Error code 03: Checksum Failed");
            throw new BankException(BankResponseCode.CHECKSUM_ERROR, "checkSum failed");
        }
        log.info("End validate checksum: Success");
    }

    // Hàm để set dữ liệu vào Redis
    //TODO: tìm hiểu try catch resource
    private void setDataToRedis(String bankCode, String tokenKey, String jsonData) {
        try {
            redisTemplate.opsForHash().put(bankCode, tokenKey, jsonData);
        } catch (Exception e) {
            log.error("Error while trying to save data to Redis for bankCode: {}, tokenKey: {}. Error: {}", bankCode, tokenKey, e.getMessage(), e);

            throw new BankException(BankResponseCode.LOST_CONNECTION_TO_REDIS, "Lost connection to redis");
        }
    }

    private void checkPrivateKey(String bankCode, String privateKey) {
        boolean isValid = validatePrivateKey(bankCode, privateKey);
        if (isValid) {
            log.info("privateKey belongs to bankCode");
        } else {
            log.info("Error code 02: PrivateKey does not belong to bankCode");
            throw new BankException(BankResponseCode.BANK_CODE_ERROR, "Invalid privateKey for bankCode");
        }
    }

    private boolean validatePrivateKey(String bankCode, String privateKey) {
        return banks.getBankList().stream()
                .filter(bank -> bank.getBankCode().equals(bankCode))
                .anyMatch(bank -> bank.getPrivateKey().equals(privateKey));
    }

}
