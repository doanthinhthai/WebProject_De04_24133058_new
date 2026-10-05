package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService_24133058 {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:temporary8386@gmail.com}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            if (mailSender == null) {
                System.out.println(">>> MÃ OTP XÁC THỰC CỦA BẠN LÀ: " + otpCode);
                return;
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Mã OTP Kích Hoạt Tài Khoản - VideoHub - 04");
            helper.setText("<h3>Xin chào!</h3><p>Mã OTP kích hoạt tài khoản của bạn là: <b style='font-size:24px; color:red;'>" + otpCode + "</b></p>", true);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(">>> Lỗi gửi mail (In ra console để test): Mã OTP là: " + otpCode);
            e.printStackTrace();
        }
    }
}