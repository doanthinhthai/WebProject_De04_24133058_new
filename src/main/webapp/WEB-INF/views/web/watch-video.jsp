<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head><title>Xem video đã mua</title></head>
<body>
<div class="card shadow-sm p-4">
    <h3 class="mb-2">${video.title}</h3>
    <p class="text-muted">Đơn hàng #${orderId}</p>

    <c:choose>
        <c:when test="${fn:contains(videoUrl, 'youtube.com/embed/') || fn:contains(videoUrl, 'player.vimeo.com/')}">
            <div class="ratio ratio-16x9">
                <iframe src="${videoUrl}" title="${video.title}"
                        allow="accelerometer; autoplay; encrypted-media; picture-in-picture"
                        allowfullscreen></iframe>
            </div>
        </c:when>
        <c:otherwise>
            <video class="w-100" controls controlsList="nodownload">
                <source src="${videoUrl}">
                Trình duyệt không hỗ trợ phát video.
            </video>
        </c:otherwise>
    </c:choose>

    <div class="mt-3">
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/orders">
            Quay lại lịch sử đơn hàng
        </a>
    </div>
</div>
</body>
</html>
