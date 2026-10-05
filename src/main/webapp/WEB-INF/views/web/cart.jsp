<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<title>Giỏ hàng</title>
</head>
<body>

	<h3 class="fw-bold mb-4">Giỏ hàng</h3>

	<c:if test="${not empty success}">
		<div class="alert alert-success">${success}</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<c:choose>
		<c:when
			test="${empty sessionScope.cart or empty sessionScope.cart.items}">
			<div class="alert alert-info">Giỏ hàng đang trống.</div>

			<a href="${pageContext.request.contextPath}/videos"
				class="btn btn-primary"> Tiếp tục chọn video </a>
		</c:when>

		<c:otherwise>
			<div class="table-responsive">
				<table class="table table-bordered align-middle">
					<thead class="table-dark">
						<tr>
							<th>Sản phẩm</th>
							<th>Đơn giá</th>
							<th style="width: 220px">Số lượng</th>
							<th>Thành tiền</th>
							<th></th>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${sessionScope.cart.items}" var="item">

							<tr>
								<td>${item.video.title}</td>

								<td><fmt:formatNumber value="${item.video.price}"
										pattern="#,##0" /> VNĐ</td>

								<td>
									<form action="${pageContext.request.contextPath}/cart/update"
										method="post" class="d-flex gap-2">

										<input type="hidden" name="videoId"
											value="${item.video.videoId}"> <input type="number"
											name="quantity" value="${item.quantity}" min="1"
											max="${item.video.stock}" class="form-control">

										<button class="btn btn-warning">Sửa</button>
									</form>
								</td>

								<td><fmt:formatNumber value="${item.subtotal}"
										pattern="#,##0" /> VNĐ</td>

								<td>
									<form action="${pageContext.request.contextPath}/cart/remove"
										method="post">

										<input type="hidden" name="videoId"
											value="${item.video.videoId}">

										<button class="btn btn-danger">Xóa</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<div class="text-end">
				<h4>
					Tổng cộng: <span class="text-danger"> <fmt:formatNumber
							value="${sessionScope.cart.totalAmount}" pattern="#,##0" /> VNĐ
					</span>
				</h4>

				<a href="${pageContext.request.contextPath}/videos"
					class="btn btn-outline-primary"> Tiếp tục chọn video </a> <a
					href="${pageContext.request.contextPath}/checkout?paymentMethod=VNPAY"
					class="btn btn-primary"> Thanh toán VNPay </a> <a
					href="${pageContext.request.contextPath}/checkout?paymentMethod=COD"
					class="btn btn-success"> Thanh toán COD </a>
			</div>
		</c:otherwise>
	</c:choose>

</body>
</html>
