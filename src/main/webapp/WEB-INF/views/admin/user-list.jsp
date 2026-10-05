<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Quản lý Users</title>
</head>
<body>
	<div class="card shadow-sm p-4 border-0">
		<div class="d-flex justify-content-between align-items-center mb-3">
			<h4 class="fw-bold mb-0">
				<i class="bi bi-people-fill text-primary"></i> DANH SÁCH USERS (Phân
				trang 6 user/trang)
			</h4>
			<a href="${pageContext.request.contextPath}/admin/users/create"
				class="btn btn-primary btn-sm"> <i class="bi bi-plus-circle"></i>
				Thêm User mới
			</a>
		</div>

		<div class="table-responsive">
			<table class="table table-bordered table-hover align-middle">
				<thead class="table-dark">
					<tr>
						<th>Username</th>
						<th>Fullname</th>
						<th>Email</th>
						<th>Phone</th>
						<th>Role</th>
						<th>Status</th>
						<th class="text-center" style="width: 220px;">Thao tác</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${users}" var="u">
						<tr>
							<td class="fw-bold">${u.username}</td>
							<td>${u.fullname}</td>
							<td>${u.email}</td>
							<td>${u.phone}</td>
							<td><c:choose>
									<c:when test="${u.admin}">
										<span class="badge bg-danger">Admin</span>
									</c:when>
									<c:otherwise>
										<span class="badge bg-secondary">User</span>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${u.active}">
										<span class="badge bg-success">Active</span>
									</c:when>
									<c:otherwise>
										<span class="badge bg-warning text-dark">Inactive</span>
									</c:otherwise>
								</c:choose></td>
							<td class="text-center">
								<button type="button"
									class="btn btn-info btn-sm text-white me-1"
									onclick="showUserDetail('${u.username}', '${u.fullname}', '${u.email}', '${u.phone}', '${u.admin ? 'Quản trị viên (Admin)' : 'Người dùng (User)'}', '${u.active ? 'Đang hoạt động' : 'Chưa kích hoạt'}')"
									title="Xem chi tiết">
									<i class="bi bi-eye"></i> Xem
								</button> <a
								href="${pageContext.request.contextPath}/admin/users/edit/${u.username}"
								class="btn btn-warning btn-sm me-1" title="Sửa"> <i
									class="bi bi-pencil"></i> Sửa
							</a> 
								<button type="button" class="btn btn-danger btn-sm"
									onclick="confirmDelete('${u.username}', '${u.fullname}')"
									title="Xóa">
									<i class="bi bi-trash"></i>
								</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<nav class="d-flex justify-content-center mt-3">
			<ul class="pagination">
				<c:forEach begin="1" end="${totalPages}" var="i">
					<li class="page-item ${currentPage == i ? 'active' : ''}"><a
						class="page-link"
						href="${pageContext.request.contextPath}/admin/users?page=${i}">${i}</a>
					</li>
				</c:forEach>
			</ul>
		</nav>
	</div>

	<div class="modal fade" id="viewUserModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content shadow">
				<div class="modal-header bg-info text-white">
					<h5 class="modal-title">
						<i class="bi bi-person-lines-fill me-2"></i> Chi Tiết Người Dùng
					</h5>
					<button type="button" class="btn-close btn-close-white"
						data-bs-dismiss="modal"></button>
				</div>
				<div class="modal-body text-start py-3">
					<p class="mb-2">
						<strong>Tên đăng nhập (Username):</strong> <span id="vUsername"
							class="text-primary fw-bold"></span>
					</p>
					<p class="mb-2">
						<strong>Họ và tên:</strong> <span id="vFullname"></span>
					</p>
					<p class="mb-2">
						<strong>Email:</strong> <span id="vEmail"></span>
					</p>
					<p class="mb-2">
						<strong>Số điện thoại:</strong> <span id="vPhone"></span>
					</p>
					<p class="mb-2">
						<strong>Vai trò:</strong> <span id="vRole"></span>
					</p>
					<p class="mb-0">
						<strong>Trạng thái:</strong> <span id="vStatus"></span>
					</p>
				</div>
				<div class="modal-footer bg-light">
					<button type="button" class="btn btn-secondary btn-sm"
						data-bs-dismiss="modal">Đóng</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="deleteModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content shadow border-0">
				<div class="modal-header bg-danger text-white">
					<h5 class="modal-title">
						<i class="bi bi-exclamation-triangle-fill me-2"></i> Xác Nhận Xóa
					</h5>
					<button type="button" class="btn-close btn-close-white"
						data-bs-dismiss="modal"></button>
				</div>
				<div class="modal-body text-start py-4">
					<p class="mb-2 fs-6">
						Bạn có chắc chắn muốn xóa người dùng <strong id="delUsername"
							class="text-danger"></strong>?
					</p>
					<p class="text-muted small mb-0">
						<i class="bi bi-info-circle"></i> Hành động này sẽ xóa dữ liệu
						người dùng khỏi hệ thống.
					</p>
				</div>
				<div class="modal-footer bg-light">
					<button type="button" class="btn btn-secondary btn-sm"
						data-bs-dismiss="modal">Hủy bỏ</button>
					<a id="delBtnConfirm" href="#" class="btn btn-danger btn-sm"> <i
						class="bi bi-trash-fill"></i> Xác nhận xóa
					</a>
				</div>
			</div>
		</div>
	</div>

	<script>
function showUserDetail(username, fullname, email, phone, role, status) {
    document.getElementById('vUsername').innerText = username;
    document.getElementById('vFullname').innerText = fullname;
    document.getElementById('vEmail').innerText = email;
    document.getElementById('vPhone').innerText = phone;
    document.getElementById('vRole').innerText = role;
    document.getElementById('vStatus').innerText = status;
    var modal = new bootstrap.Modal(document.getElementById('viewUserModal'));
    modal.show();
}

function confirmDelete(username, fullname) {
    document.getElementById('delUsername').innerText = username + " (" + fullname + ")";
    document.getElementById('delBtnConfirm').href = '${pageContext.request.contextPath}/admin/users/delete/' + username;
    var modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();
}
</script>
</body>
</html>