package vn.vnpay.bank_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import vn.vnpay.bank_demo.config.BankProperties;

@SpringBootApplication
@EnableConfigurationProperties(BankProperties.class)
public class BankDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankDemoApplication.class, args);
    }

}
