package vn.vnpay.bank_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankDemoApplication {

    public static void main(String[] args) {
        System.setProperty("spring.amqp.deserialization.trust.all", "true");
        SpringApplication.run(BankDemoApplication.class, args);
    }

}
