<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang Chủ</title>
</head>
<body>
    <div class="p-5 mb-4 bg-white rounded-3 shadow-sm border">
        <div class="container-fluid py-3">
            <h1 class="display-6 fw-bold text-primary"><i class="bi bi-play-circle me-2"></i> Chào mừng đến với VideoHub!</h1>
            <p class="col-md-8 fs-5 text-muted">Hệ thống quản lý và chia sẻ video - 04.</p>
            <a href="${pageContext.request.contextPath}/videos" class="btn btn-primary btn-lg">
                <i class="bi bi-play-btn me-1"></i> Xem danh sách Video 
            </a>
        </div>
    </div>
</body>
</html>