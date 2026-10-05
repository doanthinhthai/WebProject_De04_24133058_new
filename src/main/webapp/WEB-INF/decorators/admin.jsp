<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property='title'/> - Admin Panel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { min-height: 100vh; display: flex; flex-direction: column; background-color: #f4f6f9; }
        .sidebar { min-height: calc(100vh - 56px); background: #212529; }
        .sidebar .nav-link { color: #cfd4da; margin-bottom: 4px; border-radius: 6px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { color: #fff; background: #0d6efd; }
    </style>
    <sitemesh:write property='head'/>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top border-bottom border-secondary">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold text-warning" href="${pageContext.request.contextPath}/admin/home">
                <i class="bi bi-shield-check"></i> TRANG QUẢN TRỊ ADMIN
            </a>
            <div class="d-flex align-items-center text-white">
                <span class="me-3">Admin: <strong>${sessionScope.account.fullname}</strong></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-3 col-lg-2 d-md-block sidebar py-3">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                            <i class="bi bi-people-fill me-2"></i> Quản lý Users
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">
                            <i class="bi bi-arrow-left-circle me-2"></i> Về Trang chủ User
                        </a>
                    </li>
                </ul>
            </nav>

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <sitemesh:write property='body'/>
            </main>
        </div>
    </div>

    <footer class="bg-dark text-white text-center py-3 border-top border-secondary mt-auto">
        <small><strong>Thái Doãn Thịnh</strong> - <strong>24133058</strong> - Mã đề: <strong>Đề số 04</strong></small>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>