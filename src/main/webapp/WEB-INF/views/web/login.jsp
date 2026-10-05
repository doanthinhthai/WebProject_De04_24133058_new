<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html>
<html>
<head><title>Đăng Nhập</title></head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm p-4 border-0">
            <h3 class="fw-bold text-center text-primary mb-3">ĐĂNG NHẬP</h3>
            <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
            <c:if test="${not empty msg}"><div class="alert alert-success">${msg}</div></c:if>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Tên đăng nhập</label>
                    <input type="text" name="username" class="form-control" required placeholder="admin hoặc user1">
                </div>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Mật khẩu</label>
                    <input type="password" name="password" class="form-control" required placeholder="123">
                </div>
                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">Đăng nhập</button>
            </form>
            <div class="text-center mt-3">
                Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>