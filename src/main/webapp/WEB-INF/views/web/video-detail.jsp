<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Chi Tiết Video - ${v.title}</title>
</head>
<body>
	<div class="card shadow-sm p-4 border-0">
		<div class="row">
			<div class="col-md-4 text-center">
				<img
					src="${pageContext.request.contextPath}/static/images/${v.poster}"
					onerror="this.src='https://via.placeholder.com/350x250?text=Video+Poster'"
					class="img-fluid rounded shadow-sm border" alt="${v.title}">
			</div>
			<div class="col-md-8">
				<h4 class="fw-bold text-primary">Tiêu đề: ${v.title}</h4>
				<p class="mb-1">
					<strong>Mã video:</strong> ${v.videoId}
				</p>
				<p class="mb-1">
					<strong>Category name:</strong> <span
						class="badge bg-info text-dark">${v.categoryName}</span>
				</p>
				<p class="mb-1">
					<strong>View:</strong> ${v.views}
				</p>
				<p class="mb-1 text-primary">
					<strong>Share(${v.shareCount})</strong> &nbsp;&nbsp;|&nbsp;&nbsp; <strong
						class="text-danger">Like(${v.likeCount})</strong>
				</p>
				<c:if
					test="${sessionScope.account != null && sessionScope.account.admin}">
					<div class="mt-3 pt-3 border-top">
						<span class="badge bg-warning text-dark me-2"><i
							class="bi bi-shield-lock"></i> Quyền Quản trị viên:</span>
						<button class="btn btn-sm btn-primary me-2">
							<i class="bi bi-pencil-square"></i> Sửa video này
						</button>
						<button class="btn btn-sm btn-danger">
							<i class="bi bi-trash"></i> Xóa video
						</button>
					</div>
				</c:if>
			</div>
		</div>
		<hr>
		<div>
			<h5 class="fw-bold">description:</h5>
			<p class="text-muted">${v.description}</p>
		</div>
	</div>
</body>
</html>