package vn.vnpay.bank_demo.config.bank;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class BankConfig {
    @Value("${bank.file.path}")
    private String bankFilePath;
    @Bean
    public Banks banks() throws JAXBException {
        File file = new File(bankFilePath);
        JAXBContext jaxbContext = JAXBContext.newInstance(Banks.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Banks) jaxbUnmarshaller.unmarshal(file);
    }
    @Bean
    public List<String> bankCodes(Banks banks) {
        return banks.getBankList().stream()
                .map(Bank::getBankCode)
                .collect(Collectors.toList());
    }

    public Optional<Bank> getBankByCode(Banks banks, String bankCode) {
        return banks.getBankList().stream()
                .filter(bank -> bank.getBankCode().equals(bankCode))
                .findFirst();
    }


}
