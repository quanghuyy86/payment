package vn.vnpay.bank_demo.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.vnpay.bank_demo.common.enums.BankResponseCode;
import vn.vnpay.bank_demo.common.exception.BankException;
import vn.vnpay.bank_demo.model.dto.auth.request.AuthenticationReqDTO;
import vn.vnpay.bank_demo.model.dto.auth.response.AuthenticationResDto;
import vn.vnpay.bank_demo.model.entity.Users;
import vn.vnpay.bank_demo.repository.UsersRepository;
import vn.vnpay.bank_demo.service.AuthenticationService;
import vn.vnpay.bank_demo.util.TokenKeyUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsersRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Override
    public AuthenticationResDto authenticate(AuthenticationReqDTO authenticationReqDTO) {
        Users users = userRepository.findByUserName(authenticationReqDTO.getUserName())
                .orElseThrow(() -> new BankException(BankResponseCode.SQL_ERROR,"Not found user with username = " + authenticationReqDTO.getUserName()));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(authenticationReqDTO.getPassword(), users.getPassword());

        if (!authenticated) {
            throw new BankException(BankResponseCode.SQL_ERROR, "Không được xác thực");
        }

        String token = generateToken(authenticationReqDTO.getUserName());

        return AuthenticationResDto.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public String createTokenKey() {
        return TokenKeyUtil.generateTokenKey();
    }

    private String generateToken(String userName) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .issuer("http://localhost:8081/vnpay/")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("customClaim", "Custom")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}
