<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <title>Bảng Điều Khiển Admin</title>
</head>
<body>
    <div class="card shadow-sm p-4 border-0">
        <h3 class="fw-bold text-dark mb-3"><i class="bi bi-speedometer2 me-2 text-primary"></i> Bảng điều khiển Quản trị</h3>
        <p class="text-muted">Chào mừng Quản trị viên! Vui lòng chọn chức năng quản lý từ menu bên trái.</p>
        <div>
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary">
                <i class="bi bi-people me-1"></i> Quản lý Users
            </a>
        </div>
    </div>
</body>