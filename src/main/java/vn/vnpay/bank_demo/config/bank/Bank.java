package vn.vnpay.bank_demo.config.bank;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Bank {
    @XmlElement(name = "bankCode")
    private String bankCode;
    @XmlElement(name = "privateKey")
    private String privateKey;
    @XmlElement(name = "ips")
    private List<String> ips;
}
