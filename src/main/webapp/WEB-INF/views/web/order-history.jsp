<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"
    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"
    uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lịch sử đặt hàng</title>
</head>
<body>

<h3 class="fw-bold mb-3">Lịch sử đặt hàng</h3>

<c:if test="${not empty success}">
    <div class="alert alert-success">${success}</div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<form action="${pageContext.request.contextPath}/orders"
      method="get"
      class="row g-2 mb-4">

    <div class="col-md-5">
        <select name="status" class="form-select">
            <option value="">Tất cả trạng thái</option>

            <c:forEach items="${statuses}" var="s">
                <option value="${s.name()}"
                    ${selectedStatus == s ? 'selected' : ''}>
                    ${s.label}
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="col-auto">
        <button class="btn btn-primary">Lọc</button>
    </div>
</form>

<c:if test="${empty orders}">
    <div class="alert alert-info">
        Không có đơn hàng phù hợp.
    </div>
</c:if>

<c:forEach items="${orders}" var="order">
    <div class="card shadow-sm mb-4">
        <div class="card-header d-flex
                    justify-content-between">

            <strong>Đơn hàng #${order.orderId}</strong>

            <span class="badge bg-${order.status.badgeClass}">
                ${order.status.label}
            </span>
        </div>

        <div class="card-body">
            <p>
                <strong>Ngày đặt:</strong>
                ${order.createdAt}
            </p>

            <p>
                <strong>Người nhận:</strong>
                ${order.receiverName} – ${order.phone}
            </p>

            <p>
                <strong>Địa chỉ:</strong>
                ${order.address}
            </p>

            <p>
                <strong>Thanh toán:</strong>
                ${order.paymentMethod}
                <span class="badge bg-${order.paymentStatus.badgeClass} ms-2">
                    ${order.paymentStatus.label}
                </span>
            </p>

            <c:if test="${not empty order.paymentTransactionNo}">
                <p><strong>Mã giao dịch VNPay:</strong> ${order.paymentTransactionNo}</p>
            </c:if>

            <table class="table table-sm">
                <thead>
                    <tr>
                        <th>Sản phẩm</th>
                        <th>Đơn giá</th>
                        <th>Số lượng</th>
                        <th>Thành tiền</th>
                        <th>Video online</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach items="${order.items}" var="item">
                        <tr>
                            <td>${item.videoTitle}</td>
                            <td>
                                <fmt:formatNumber
                                    value="${item.unitPrice}"
                                    pattern="#,##0"/>
                            </td>
                            <td>${item.quantity}</td>
                            <td>
                                <fmt:formatNumber
                                    value="${item.subtotal}"
                                    pattern="#,##0"/>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${order.videoAccessGranted}">
                                        <a class="btn btn-sm btn-primary"
                                           href="${pageContext.request.contextPath}/orders/${order.orderId}/watch/${item.videoId}">
                                            Xem video
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted small">Chưa mở quyền</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h5 class="text-end text-danger">
                Tổng:
                <fmt:formatNumber
                    value="${order.totalAmount}"
                    pattern="#,##0"/> VNĐ
            </h5>
        </div>
    </div>
</c:forEach>

</body>
</html>
