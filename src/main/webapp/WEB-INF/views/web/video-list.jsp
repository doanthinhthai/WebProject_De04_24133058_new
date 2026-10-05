<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Danh sách Video</title>
</head>

<body>

	<c:if test="${not empty success}">
		<div class="alert alert-success alert-dismissible fade show">
			${success}
			<button type="button" class="btn-close" data-bs-dismiss="alert">
			</button>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="alert alert-danger alert-dismissible fade show">
			${error}
			<button type="button" class="btn-close" data-bs-dismiss="alert">
			</button>
		</div>
	</c:if>

	<div class="mb-4 d-flex justify-content-between align-items-center">
		<h3 class="fw-bold text-dark mb-0">
			${currentCat.categoryname} <span class="text-danger">
				(${totalVideos}) </span>
		</h3>

		<div class="dropdown">
			<button class="btn btn-outline-primary dropdown-toggle" type="button"
				data-bs-toggle="dropdown">Chọn danh mục khác</button>

			<ul class="dropdown-menu dropdown-menu-end">
				<c:forEach items="${allCategories}" var="cat">
					<li><a class="dropdown-item"
						href="${pageContext.request.contextPath}/videos?categoryId=${cat.categoryId}">
							${cat.categoryname} (${cat.videoCount}) </a></li>
				</c:forEach>
			</ul>
		</div>
	</div>

	<div class="row row-cols-1 row-cols-md-3 g-4 mb-4">
		<c:forEach items="${videos}" var="video">
			<div class="col">
				<div class="card h-100 shadow-sm border-0">

					<img
						src="${pageContext.request.contextPath}/static/images/${video.poster}"
						onerror="this.src='https://via.placeholder.com/300x200?text=Video'"
						class="card-img-top" style="height: 180px; object-fit: cover;"
						alt="${video.title}">

					<div class="card-body d-flex flex-column">
						<h5 class="card-title fw-bold text-truncate">
							<a
								href="${pageContext.request.contextPath}/video/detail?id=${video.videoId}"
								class="text-decoration-none text-dark"> ${video.title} </a>
						</h5>

						<p class="card-text mb-1 small text-muted">
							<strong>Mã video:</strong> ${video.videoId}
						</p>

						<p class="card-text mb-1 small text-muted">
							<strong>Category name:</strong> ${video.categoryName}
						</p>

						<p class="card-text mb-1 small text-muted">
							<strong>View:</strong> ${video.views}
						</p>

						<p class="card-text small text-primary mb-2">
							<strong>Share(${video.shareCount})</strong> &bull; <strong
								class="text-danger"> Like(${video.likeCount}) </strong>
						</p>

						<hr class="mt-auto">

						<div
							class="d-flex justify-content-between align-items-center mb-2">
							<span class="fw-semibold">Giá:</span> <span
								class="text-danger fw-bold fs-5"> <fmt:formatNumber
									value="${video.price}" type="number" pattern="#,##0" /> VNĐ
							</span>
						</div>

						<p class="small mb-3">
							<strong>Tồn kho:</strong>

							<c:choose>
								<c:when test="${video.stock > 0}">
									<span class="text-success"> Còn ${video.stock} video </span>
								</c:when>

								<c:otherwise>
									<span class="text-danger"> Hết hàng </span>
								</c:otherwise>
							</c:choose>
						</p>

						<c:choose>
							<c:when test="${video.stock > 0}">
								<form action="${pageContext.request.contextPath}/cart/add-video"
									method="post" class="d-flex gap-2">

									<input type="hidden" name="videoId" value="${video.videoId}">

									<input type="number" name="quantity" value="1" min="1"
										max="${video.stock}" required class="form-control"
										style="max-width: 90px;">

									<button type="submit" class="btn btn-primary flex-grow-1">
										<i class="bi bi-cart-plus"></i> Thêm vào giỏ
									</button>
								</form>
							</c:when>

							<c:otherwise>
								<button type="button" class="btn btn-secondary w-100" disabled>
									<i class="bi bi-cart-x"></i> Đã hết hàng
								</button>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

	<c:if test="${empty videos}">
		<div class="alert alert-info text-center">Danh mục này chưa có
			video.</div>
	</c:if>

	<c:if test="${totalPages > 0}">
		<nav class="d-flex justify-content-center">
			<ul class="pagination">

				<li class="page-item ${currentPage == 1 ? 'disabled' : ''}"><a
					class="page-link"
					href="${pageContext.request.contextPath}/videos?categoryId=${categoryId}&page=${currentPage - 1}">
						&laquo; </a></li>

				<c:forEach begin="1" end="${totalPages}" var="i">
					<li class="page-item ${currentPage == i ? 'active' : ''}"><a
						class="page-link"
						href="${pageContext.request.contextPath}/videos?categoryId=${categoryId}&page=${i}">
							${i} </a></li>
				</c:forEach>

				<li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
					<a class="page-link"
					href="${pageContext.request.contextPath}/videos?categoryId=${categoryId}&page=${currentPage + 1}">
						&raquo; </a>
				</li>

			</ul>
		</nav>
	</c:if>

</body>
</html>