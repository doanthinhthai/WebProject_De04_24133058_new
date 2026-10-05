package com.example.demo.service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VnpayService_24133058 {
    private static final DateTimeFormatter VNPAY_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    @Value("${vnpay.tmn-code:}")
    private String tmnCode;

    @Value("${vnpay.hash-secret:}")
    private String hashSecret;

    @Value("${vnpay.pay-url:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}")
    private String payUrl;

    @Value("${vnpay.return-url:http://localhost:8081/payment/vnpay-return}")
    private String returnUrl;

    public boolean isConfigured() {
        return tmnCode != null && !tmnCode.isBlank()
                && hashSecret != null && !hashSecret.isBlank();
    }

    public String createPaymentUrl(long orderId, BigDecimal amount, String clientIp) {
        if (!isConfigured()) {
            throw new IllegalStateException("VNPay chưa được cấu hình TmnCode và HashSecret");
        }

        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode.trim());
        params.put("vnp_Amount", amount.multiply(BigDecimal.valueOf(100)).toBigIntegerExact().toString());
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", String.valueOf(orderId));
        params.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", normalizeIp(clientIp));
        params.put("vnp_CreateDate", now.format(VNPAY_TIME));
        params.put("vnp_ExpireDate", now.plusMinutes(15).format(VNPAY_TIME));

        String signedData = buildQuery(params);
        return payUrl + "?" + signedData + "&vnp_SecureHash=" + hmacSha512(hashSecret, signedData);
    }

    public boolean validateResponse(Map<String, String> responseParams) {
        if (!isConfigured()) return false;
        String receivedHash = responseParams.get("vnp_SecureHash");
        if (receivedHash == null || receivedHash.isBlank()) return false;

        Map<String, String> signedFields = new TreeMap<>();
        responseParams.forEach((key, value) -> {
            if (key.startsWith("vnp_")
                    && !"vnp_SecureHash".equals(key)
                    && !"vnp_SecureHashType".equals(key)
                    && value != null && !value.isEmpty()) {
                signedFields.put(key, value);
            }
        });
        String expectedHash = hmacSha512(hashSecret, buildQuery(signedFields));
        return MessageDigest.isEqual(expectedHash.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.US_ASCII),
                receivedHash.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.US_ASCII));
    }

    private String buildQuery(Map<String, String> params) {
        StringJoiner joiner = new StringJoiner("&");
        params.forEach((key, value) -> joiner.add(encode(key) + "=" + encode(value)));
        return joiner.toString();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.US_ASCII);
    }

    private String hmacSha512(String secret, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(bytes.length * 2);
            for (byte value : bytes) result.append(String.format("%02x", value));
            return result.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Không thể tạo chữ ký VNPay", e);
        }
    }

    private String normalizeIp(String ip) {
        if (ip == null || ip.isBlank() || "0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }
        int comma = ip.indexOf(',');
        return (comma >= 0 ? ip.substring(0, comma) : ip).trim();
    }
}
