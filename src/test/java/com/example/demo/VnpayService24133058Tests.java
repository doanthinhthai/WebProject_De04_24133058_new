package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import com.example.demo.service.VnpayService_24133058;

class VnpayService24133058Tests {
    @Test
    void generatedUrlHasValidSignatureAndRejectsTampering() {
        VnpayService_24133058 service = new VnpayService_24133058();
        ReflectionTestUtils.setField(service, "tmnCode", "TESTCODE");
        ReflectionTestUtils.setField(service, "hashSecret", "test-secret-24133058");
        ReflectionTestUtils.setField(service, "payUrl", "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
        ReflectionTestUtils.setField(service, "returnUrl", "http://localhost:8081/payment/vnpay-return");

        String url = service.createPaymentUrl(15L, new BigDecimal("70000"), "127.0.0.1");
        Map<String, String> params = parseQuery(url.substring(url.indexOf('?') + 1));

        assertEquals("7000000", params.get("vnp_Amount"));
        assertEquals("15", params.get("vnp_TxnRef"));
        assertTrue(service.validateResponse(params));

        params.put("vnp_Amount", "100");
        assertFalse(service.validateResponse(params));
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> values = new LinkedHashMap<>();
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            values.put(URLDecoder.decode(parts[0], StandardCharsets.US_ASCII),
                    URLDecoder.decode(parts[1], StandardCharsets.US_ASCII));
        }
        return values;
    }
}
