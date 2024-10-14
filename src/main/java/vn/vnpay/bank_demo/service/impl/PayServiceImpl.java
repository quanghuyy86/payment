package vn.vnpay.bank_demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.vnpay.bank_demo.common.enums.BankResponseCode;
import vn.vnpay.bank_demo.common.exception.BankException;
import vn.vnpay.bank_demo.config.bank.Banks;
import vn.vnpay.bank_demo.config.rabitmq.RabbitMQConfig;
import vn.vnpay.bank_demo.model.dto.payment.request.CheckSumRequestDTO;
import vn.vnpay.common.PaymentRequestDTO;
import vn.vnpay.common.PaymentConsumerResponse;
import vn.vnpay.bank_demo.repository.PaymentRepository;
import vn.vnpay.bank_demo.service.PayService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static vn.vnpay.bank_demo.util.CheckSumUtil.calculateCheckSum;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayServiceImpl implements PayService {

    private final PaymentRepository paymentRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Banks banks;
    private final Gson gson;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    public final String TOKEN_KEY = "TokenKey_" + LocalDate.now();


    @Override
    public void createPayment(PaymentRequestDTO request) {
        log.info("Begin create-payment: {}", gson.toJson(request));
        //Validate tokenKey
        //TODO: setIfAbsent
        // nhung kieu du lieu nao
        // set expire
        boolean exists = redisTemplate.opsForHash().hasKey(TOKEN_KEY, request.getTokenKey());
        if (exists){
            log.info("TokenKey is duplicated");
            throw new BankException(BankResponseCode.TOKENKEY_DUPLICATED, "TokenKey is duplicated");
        }
        //Validate Promotion Code
        validatePromotionCode(request);

        //Check bankCode
        validateBankCode(request.getBankCode());

        //Check privateKey
        checkPrivateKey(request.getBankCode(), request.getPrivateKey());

        //Kiểm tra checkSum
        validateCheckSum(request);

        //đẩy dũ liệu lên rabbit
//        setDataToRabbitMQ(request);


        log.info("TokenKey: " + request.getTokenKey() + " expiration time: " + getSecondsUntilMidnight() + "S"); // thời gian hết hạn cho đến 0h ngày hôm sau
        redisTemplate.opsForHash().put(this.TOKEN_KEY, request.getTokenKey(), request.getTokenKey());
        for (int i= 0; i<1000000; i++){
            rabbitTemplate.convertAndSend("myQueue","hello");
        }
        log.info("End create-payment: Success");
    }

    private void setDataToRabbitMQ(PaymentRequestDTO request) {
        try {
            // Gửi dữ liệu đến RabbitMQ và nhận phản hồi
//            PaymentConsumerResponse consumerResponse = (PaymentConsumerResponse) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.QUEUE_NAME, request);
            PaymentConsumerResponse consumerResponse = (PaymentConsumerResponse) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.QUEUE_NAME, request);
            log.info("Response Consumer: {}" , gson.toJson( consumerResponse));

            // Kiểm tra phản hồi từ Consumer
            if (consumerResponse == null) {
                throw new BankException(BankResponseCode.BAD_GATEWAY_ERROR, "Not found consumer");
            }

            // Chuyển đổi phản hồi JSON thành đối tượng PaymentConsumerResponse
//            PaymentConsumerResponse paymentConsumerResponse = objectMapper.readValue(consumerResponse, PaymentConsumerResponse.class);

            // Kiểm tra mã phản hồi
            if ("99".equals(consumerResponse.getCode())) {//TODO: enum error code
                throw new BankException(BankResponseCode.BAD_GATEWAY_ERROR, consumerResponse.getMessage());
            }

        } catch (Exception e) {
            log.error("Unexpected error: ",e);
            throw new BankException(BankResponseCode.BAD_GATEWAY_ERROR, "An unexpected error occurred");
        }
    }

    private void validateTokenKey(PaymentRequestDTO request) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(request.getTokenKey()))) {
            log.info("TokenKey is duplicated");
            throw new BankException(BankResponseCode.TOKENKEY_DUPLICATED, "TokenKey is duplicated");
        } else {
            // thời gian hết hạn cho đến 0h ngày hôm sau
            long secondsUntilMidnight = getSecondsUntilMidnight();
            log.info("Expiration time: " + secondsUntilMidnight);
            // Lưu tokenKey vào Redis với thời gian hết hạn tính bằng giây
            redisTemplate.opsForValue().set(request.getTokenKey(), request.getTokenKey(), secondsUntilMidnight, TimeUnit.SECONDS);
        }
    }

    private void setRedis(PaymentRequestDTO request) {
        String dataReq = gson.toJson(request);
        setDataToRedis(request.getBankCode(), request.getTokenKey(), dataReq);
    }


    private void validateBankCode(String bankCode) {
        boolean exist = banks.getBankList().stream()
                .anyMatch(bank -> bank.getBankCode().equals(bankCode));
        if (!exist) {
            log.info("Error code 02: BankCode Failed");
            throw new BankException(BankResponseCode.BANK_CODE_ERROR, "bankCode Failed");
        }
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
        String input = request.getMobile() + request.getBankCode() + request.getAccountNo()
                + request.getPayDate() + request.getDebitAmount() + request.getRespCode()
                + request.getTraceTransfer() + request.getMessageType() + request.getPrivateKey();

        String calculatedCheckSum = calculateCheckSum(input);
        log.info("Checksum request: " + request.getCheckSum());
        if (!calculatedCheckSum.equals(request.getCheckSum())) {
            log.warn("Error code 03: Checksum Failed");
            throw new BankException(BankResponseCode.CHECKSUM_ERROR, "checkSum failed");
        }
    }

    // Hàm để set dữ liệu vào Redis
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
        if (!isValid) {
            log.info("Error code 02: PrivateKey does not belong to bankCode");
            throw new BankException(BankResponseCode.BANK_CODE_ERROR, "Invalid privateKey for bankCode");
        }
    }

    private boolean validatePrivateKey(String bankCode, String privateKey) {
        return banks.getBankList().stream()
                .filter(bank -> bank.getBankCode().equals(bankCode))
                .anyMatch(bank -> bank.getPrivateKey().equals(privateKey));
    }

    private void validatePromotionCode(PaymentRequestDTO request) {
        // So sánh hai giá trị debitAmount(số tiền thanh toán) và realAmount(số tiền sau khuyến mại)
        int comparisonResult = request.getDebitAmount().compareTo(request.getRealAmount());

        if (comparisonResult < 0) { // debitAmount nhỏ hơn realAmount
            log.info("Error code 01: Số tiền thanh toán phải hơn số tiền sau khuyến mại.");
            throw new BankException(BankResponseCode.FIELD_ERROR, "Số tiền thanh toán phải lớn hơn hoặc bằng số tiền sau khuyến mại.");
        } else if (comparisonResult == 0) { // debitAmount bằng realAmount
            if (request.getPromotionCode() != null && !request.getPromotionCode().isEmpty()) {
                log.info("Error code 01: Mã Voucher phải bằng null hoặc rỗng khi không có khuyến mãi.");
                throw new BankException(BankResponseCode.FIELD_ERROR, "Mã Voucher phải bằng null hoặc rỗng khi không có khuyến mãi.");
            }
        } else { // debitAmount lớn hơn realAmount
            if (request.getPromotionCode() == null || request.getPromotionCode().isEmpty()) {
                log.info("Error code 01: Không có mã voucher, mặc dù số tiền đã giảm.");
                throw new BankException(BankResponseCode.FIELD_ERROR, "Không có mã voucher, mặc dù số tiền đã giảm.");
            }
        }
    }

    // Hàm tính số giây còn lại từ hiện tại đến 0h ngày hôm sau
    private long getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return ChronoUnit.SECONDS.between(now, midnight);
    }


}
