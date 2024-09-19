package vn.vnpay.bank_demo.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
@ConfigurationProperties(prefix = "banks")
public class BankProperties {
    private List<BankConfig> banks;

    public void setBanks(List<BankConfig> banks) {
        this.banks = banks;
    }

    @Getter
    public static class BankConfig {
        private String bankCode;
        private String privateKey;
        private List<String> ips;

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public void setIps(List<String> ips) {
            this.ips = ips;
        }
    }
}
