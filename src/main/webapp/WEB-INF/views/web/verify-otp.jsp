<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html>
<html>
<head><title>Xác Thực OTP</title></head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm p-4 border-0 text-center">
            <h3 class="fw-bold text-primary mb-2">XÁC THỰC MÃ OTP</h3>
            <p class="text-muted">Mã kích hoạt gồm 6 chữ số đã được gửi tới email của bạn.</p>
            <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
            <form action="${pageContext.request.contextPath}/verify-otp" method="post">
                <div class="mb-3">
                    <input type="text" name="otp" class="form-control form-control-lg text-center fw-bold text-danger letter-spacing" required placeholder="------" maxlength="6">
                </div>
                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">Kích hoạt tài khoản</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>