package vn.vnpay.bank_demo.config.bank;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "banks")
@XmlAccessorType(XmlAccessType.FIELD)
public class Banks {
    @XmlElement(name = "bank")
    private List<Bank> bankList;
}
