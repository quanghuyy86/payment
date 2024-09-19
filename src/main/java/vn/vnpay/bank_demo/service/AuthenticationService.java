package vn.vnpay.bank_demo.service;


import vn.vnpay.bank_demo.model.dto.auth.request.AuthenticationReqDTO;
import vn.vnpay.bank_demo.model.dto.auth.response.AuthenticationResDto;

public interface AuthenticationService {
    AuthenticationResDto authenticate(AuthenticationReqDTO authenticationReqDTO);

}
