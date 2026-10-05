<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<title>Sản phẩm</title>
</head>
<body>

	<c:if test="${not empty success}">
		<div class="alert alert-success">${success}</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<h3 class="fw-bold mb-4">Danh sách sản phẩm</h3>

	<div class="row row-cols-1 row-cols-md-3 g-4">
		<c:forEach items="${products}" var="p">
			<div class="col">
				<div class="card h-100 shadow-sm">
					<img
						src="${pageContext.request.contextPath}/static/images/${p.image}"
						onerror="this.src='https://via.placeholder.com/300x200?text=Product'"
						class="card-img-top" style="height: 200px; object-fit: cover">

					<div class="card-body">
						<h5>${p.productName}</h5>
						<p class="text-muted">${p.description}</p>

						<p class="text-danger fw-bold">
							<fmt:formatNumber value="${p.price}" pattern="#,##0" />
							VNĐ
						</p>

						<p>Còn lại: ${p.stock}</p>

						<form action="${pageContext.request.contextPath}/cart/add"
							method="post">

							<input type="hidden" name="productId" value="${p.productId}">

							<div class="input-group">
								<input type="number" name="quantity" value="1" min="1"
									max="${p.stock}" class="form-control"
									${p.stock == 0 ? 'disabled' : ''}>

								<button class="btn btn-primary"
									${p.stock == 0 ? 'disabled' : ''}>Thêm vào giỏ</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

</body>
</html>