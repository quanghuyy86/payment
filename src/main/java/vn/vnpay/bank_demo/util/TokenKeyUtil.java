package vn.vnpay.bank_demo.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class TokenKeyUtil {
    private static final SecureRandom secureRandom = new SecureRandom(); // Cơ chế ngẫu nhiên bảo mật
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); // Base64 URL encoding
    private static final Set<String> generatedTokenKeys = new HashSet<>(); // Lưu trữ các token đã tạo
    private static LocalDate currentDate = LocalDate.now(); // Ngày hiện tại

    public static String generateTokenKey() {
        // Kiểm tra nếu ngày hiện tại đã thay đổi
        if (!LocalDate.now().equals(currentDate)) {
            generatedTokenKeys.clear(); // Xóa danh sách token đã tạo khi chuyển ngày
            currentDate = LocalDate.now(); // Cập nhật ngày hiện tại
        }

        String tokenKey;
        do {
            tokenKey = createToken(); // Tạo token mới
        } while (generatedTokenKeys.contains(tokenKey)); // Kiểm tra tính duy nhất

        generatedTokenKeys.add(tokenKey); // Lưu token đã tạo
        return tokenKey;
    }

    private static String createToken() {
        byte[] randomBytes = new byte[32]; // Kích thước token (32 bytes)
        secureRandom.nextBytes(randomBytes); // Tạo ngẫu nhiên
        return base64Encoder.encodeToString(randomBytes); // Mã hóa thành chuỗi base64
    }
}
