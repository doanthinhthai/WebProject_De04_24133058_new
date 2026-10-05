<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"
    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"
    uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán đơn hàng</title>
</head>
<body>

<div class="row">
    <div class="col-md-7">
        <div class="card shadow-sm p-4">
            <h3>Thông tin nhận hàng</h3>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    ${error}
                </div>
            </c:if>

            <form
              action="${pageContext.request.contextPath}/checkout"
              method="post">

                <div class="mb-3">
                    <label class="form-label">Người nhận</label>
                    <input type="text"
                           name="receiverName"
                           value="${account.fullname}"
                           required
                           class="form-control">
                </div>

                <div class="mb-3">
                    <label class="form-label">Số điện thoại</label>
                    <input type="tel"
                           name="phone"
                           value="${account.phone}"
                           required
                           pattern="[0-9]{9,11}"
                           class="form-control">
                </div>

                <div class="mb-3">
                    <label class="form-label">Địa chỉ</label>
                    <textarea name="address"
                              required
                              maxlength="500"
                              class="form-control"></textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Ghi chú</label>
                    <textarea name="note"
                              maxlength="1000"
                              class="form-control"></textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Phương thức thanh toán</label>
                    <div class="form-check border rounded p-3 ps-5 mb-2">
                        <input class="form-check-input" type="radio" name="paymentMethod"
                               id="paymentCod" value="COD" checked>
                        <label class="form-check-label" for="paymentCod">
                            <strong>COD</strong> – thanh toán khi nhận hàng
                        </label>
                    </div>
                    <div class="form-check border rounded p-3 ps-5">
                        <input class="form-check-input" type="radio" name="paymentMethod"
                               id="paymentVnpay" value="VNPAY">
                        <label class="form-check-label" for="paymentVnpay">
                            <strong class="text-primary">VNPay Sandbox</strong> – ATM, thẻ quốc tế hoặc QR
                        </label>
                    </div>
                    <div class="form-text mt-2">
                        Thanh toán VNPay thành công sẽ mở quyền xem video trực tuyến trong lịch sử đơn hàng.
                    </div>
                </div>

                <button class="btn btn-success w-100">
                    Tiếp tục thanh toán
                </button>
            </form>
        </div>
    </div>

    <div class="col-md-5">
        <div class="card shadow-sm p-4">
            <h4>Đơn hàng</h4>

            <c:forEach
                items="${sessionScope.cart.items}"
                var="item">

                <div class="d-flex justify-content-between">
                    <span>
                        ${item.video.title}
                        x ${item.quantity}
                    </span>

                    <span>
                        <fmt:formatNumber
                            value="${item.subtotal}"
                            pattern="#,##0"/>
                    </span>
                </div>
            </c:forEach>

            <hr>

            <h5 class="text-end text-danger">
                <fmt:formatNumber
                    value="${sessionScope.cart.totalAmount}"
                    pattern="#,##0"/> VNĐ
            </h5>
        </div>
    </div>
</div>

</body>
</html>
