<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head><title>Kết quả thanh toán VNPay</title></head>
<body>
<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow-sm p-4 text-center">
            <c:choose>
                <c:when test="${paymentSuccess}">
                    <h3 class="text-success mb-3">Thanh toán thành công</h3>
                </c:when>
                <c:otherwise>
                    <h3 class="text-danger mb-3">Thanh toán chưa thành công</h3>
                </c:otherwise>
            </c:choose>
            <p>${paymentMessage}</p>
            <c:if test="${not empty orderId}">
                <p class="fw-bold">Mã đơn hàng: #${orderId}</p>
            </c:if>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/orders">
                Xem lịch sử đơn hàng
            </a>
        </div>
    </div>
</div>
</body>
</html>
