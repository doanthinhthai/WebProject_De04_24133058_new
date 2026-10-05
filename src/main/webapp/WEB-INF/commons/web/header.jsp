<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold text-primary"
           href="${pageContext.request.contextPath}/home">
            <i class="bi bi-play-circle-fill"></i> VideoHub
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navMenu">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/videos">Sản phẩm</a>
                </li>
                <c:if test="${not empty sessionScope.account and sessionScope.account.admin}">
                    <li class="nav-item">
                        <a class="nav-link text-warning fw-bold"
                           href="${pageContext.request.contextPath}/admin/home">
                            <i class="bi bi-shield-lock-fill"></i> Trang quản trị
                        </a>
                    </li>
                </c:if>
            </ul>

            <ul class="navbar-nav ms-auto align-items-lg-center">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cart">
                        <i class="bi bi-cart3"></i> Giỏ hàng
                        <span class="badge bg-danger">
                            ${empty sessionScope.cart ? 0 : sessionScope.cart.totalQuantity}
                        </span>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${empty sessionScope.account}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/orders">
                                <i class="bi bi-clock-history"></i> Lịch sử đơn hàng
                            </a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-white" href="#" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle"></i> ${sessionScope.account.fullname}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a class="dropdown-item text-danger"
                                       href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
