package vn.vnpay.bank_demo.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.bank_demo.model.dto.auth.request.AuthenticationReqDTO;
import vn.vnpay.bank_demo.model.dto.auth.response.AuthenticationResDto;
import vn.vnpay.bank_demo.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResDto> authentication(@RequestBody @Valid AuthenticationReqDTO authenticationReqDTO) {
        AuthenticationResDto result = authenticationService.authenticate(authenticationReqDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/token-key")
    public ResponseEntity<String> createTokenKey() {
        String tokenKey = authenticationService.createTokenKey();
        return ResponseEntity.ok(tokenKey);
    }
}
