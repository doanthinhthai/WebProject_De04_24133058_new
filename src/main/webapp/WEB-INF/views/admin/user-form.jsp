<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html>
<html>
<head><title>${isEdit ? 'Chỉnh Sửa User' : 'Thêm User Mới'}</title></head>
<body>
<div class="card shadow-sm p-4 border-0 col-md-8 mx-auto">
    <h4 class="fw-bold mb-3">${isEdit ? 'CHỈNH SỬA USER' : 'THÊM USER MỚI'}</h4>
    <form action="${pageContext.request.contextPath}/admin/users/save" method="post">
        <div class="mb-3">
            <label class="form-label fw-semibold">Username</label>
            <input type="text" name="username" value="${user.username}" ${isEdit ? 'readonly' : 'required'} class="form-control">
        </div>
        <div class="mb-3">
            <label class="form-label fw-semibold">Password</label>
            <input type="password" name="password" value="${user.password}" required class="form-control">
        </div>
        <div class="mb-3">
            <label class="form-label fw-semibold">Fullname</label>
            <input type="text" name="fullname" value="${user.fullname}" required class="form-control">
        </div>
        <div class="mb-3">
            <label class="form-label fw-semibold">Email</label>
            <input type="email" name="email" value="${user.email}" required class="form-control">
        </div>
        <div class="mb-3">
            <label class="form-label fw-semibold">Phone</label>
            <input type="text" name="phone" value="${user.phone}" class="form-control">
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="admin" value="true" ${user.admin ? 'checked' : ''} id="chkAdmin">
                    <label class="form-check-label fw-semibold" for="chkAdmin">Vai trò Admin</label>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="active" value="true" ${user.active ? 'checked' : ''} id="chkActive">
                    <label class="form-check-label fw-semibold" for="chkActive">Kích hoạt (Active)</label>
                </div>
            </div>
        </div>
        <div class="d-flex justify-content-between">
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Quay lại</a>
            <button type="submit" class="btn btn-primary">Lưu thông tin</button>
        </div>
    </form>
</div>
</body>
</html>