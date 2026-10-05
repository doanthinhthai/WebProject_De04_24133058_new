<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html>
<html>
<head><title>Đăng Ký Tài Khoản</title></head>
<body>
<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card shadow-sm p-4 border-0">
            <h3 class="fw-bold text-center text-primary mb-3">ĐĂNG KÝ TÀI KHOẢN</h3>
            <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Tài khoản (Username) <span class="text-danger">*</span></label>
                    <input type="text" name="username" class="form-control" required placeholder="Nhập username...">
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Mật khẩu <span class="text-danger">*</span></label>
                    <input type="password" name="password" class="form-control" required placeholder="Nhập mật khẩu...">
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Họ và tên <span class="text-danger">*</span></label>
                    <input type="text" name="fullname" class="form-control" required placeholder="Ví dụ: Nguyễn Văn A">
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Email nhận OTP <span class="text-danger">*</span></label>
                    <input type="email" name="email" class="form-control" required placeholder="email-cua-ban@gmail.com">
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Số điện thoại</label>
                    <input type="text" name="phone" class="form-control" placeholder="0901234567">
                </div>
                <button type="submit" class="btn btn-success w-100 py-2 fw-semibold">Đăng ký & Nhận mã OTP qua Email</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>