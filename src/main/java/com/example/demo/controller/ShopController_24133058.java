package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.example.demo.dao.OrderDao_24133058;
import com.example.demo.model.*;
import com.example.demo.service.VideoService_24133058;
import com.example.demo.service.VnpayService_24133058;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ShopController_24133058 {
    private final VideoService_24133058 videoService;
    private final OrderDao_24133058 orderDao;
    private final VnpayService_24133058 vnpayService;

    public ShopController_24133058(VideoService_24133058 videoService,
                                   OrderDao_24133058 orderDao,
                                   VnpayService_24133058 vnpayService) {
        this.videoService = videoService;
        this.orderDao = orderDao;
        this.vnpayService = vnpayService;
    }

    @GetMapping("/cart")
    public String cart(HttpSession session) {
        getCart(session);
        return "web/cart";
    }

    @PostMapping("/cart/add-video")
    public String addVideo(@RequestParam String videoId,
                           @RequestParam(defaultValue = "1") int quantity,
                           HttpSession session,
                           RedirectAttributes redirect) {
        Video_24133058 video = videoService.findById(videoId);
        if (video == null || !video.isActive()) {
            redirect.addFlashAttribute("error", "Video không tồn tại hoặc đã ngừng bán");
            return "redirect:/videos";
        }
        if (quantity < 1) quantity = 1;

        Cart_24133058 cart = getCart(session);
        CartItem_24133058 current = cart.getItem(videoId);
        int newQuantity = quantity + (current == null ? 0 : current.getQuantity());
        if (newQuantity > video.getStock()) {
            redirect.addFlashAttribute("error", "Video '" + video.getTitle()
                    + "' chỉ còn " + video.getStock() + " sản phẩm");
            return "redirect:/videos?categoryId=" + video.getCategoryId();
        }

        cart.put(new CartItem_24133058(video, newQuantity));
        redirect.addFlashAttribute("success", "Đã thêm '" + video.getTitle() + "' vào giỏ");
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam String videoId,
                             @RequestParam int quantity,
                             HttpSession session,
                             RedirectAttributes redirect) {
        Cart_24133058 cart = getCart(session);
        if (quantity <= 0) {
            cart.remove(videoId);
            redirect.addFlashAttribute("success", "Đã xóa video khỏi giỏ");
            return "redirect:/cart";
        }

        Video_24133058 video = videoService.findById(videoId);
        if (video == null || !video.isActive()) {
            cart.remove(videoId);
            redirect.addFlashAttribute("error", "Video không còn được bán");
            return "redirect:/cart";
        }
        if (quantity > video.getStock()) {
            redirect.addFlashAttribute("error", "Số lượng tối đa của '" + video.getTitle()
                    + "' là " + video.getStock());
            return "redirect:/cart";
        }

        cart.put(new CartItem_24133058(video, quantity));
        redirect.addFlashAttribute("success", "Đã cập nhật số lượng");
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam String videoId,
                                 HttpSession session,
                                 RedirectAttributes redirect) {
        getCart(session).remove(videoId);
        redirect.addFlashAttribute("success", "Đã xóa video khỏi giỏ");
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        User_24133058 account = getAccount(session);
        if (account == null) return "redirect:/login";
        if (getCart(session).isEmpty()) return "redirect:/cart";
        model.addAttribute("account", account);
        return "web/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String receiverName,
                           @RequestParam String phone,
                           @RequestParam String address,
                           @RequestParam(required = false) String note,
                           @RequestParam(defaultValue = "COD") String paymentMethod,
                           HttpServletRequest request,
                           HttpSession session,
                           RedirectAttributes redirect) {
        User_24133058 account = getAccount(session);
        if (account == null) return "redirect:/login";

        Cart_24133058 cart = getCart(session);
        if (cart.isEmpty()) {
            redirect.addFlashAttribute("error", "Giỏ hàng đang trống");
            return "redirect:/cart";
        }
        if (receiverName.isBlank() || phone.isBlank() || address.isBlank()) {
            redirect.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin nhận hàng");
            return "redirect:/checkout";
        }

        paymentMethod = paymentMethod.trim().toUpperCase();
        if (!"COD".equals(paymentMethod) && !"VNPAY".equals(paymentMethod)) {
            redirect.addFlashAttribute("error", "Phương thức thanh toán không hợp lệ");
            return "redirect:/checkout";
        }
        if ("VNPAY".equals(paymentMethod) && !vnpayService.isConfigured()) {
            redirect.addFlashAttribute("error", "VNPay Sandbox chưa được cấu hình TmnCode và HashSecret");
            return "redirect:/checkout";
        }

        try {
            if ("VNPAY".equals(paymentMethod)) {
                long orderId = orderDao.createVnpayOrder(account.getUsername(), receiverName.trim(),
                        phone.trim(), address.trim(), note == null ? null : note.trim(), cart);
                try {
                    String forwardedIp = request.getHeader("X-Forwarded-For");
                    String paymentUrl = vnpayService.createPaymentUrl(orderId, cart.getTotalAmount(),
                            forwardedIp == null ? request.getRemoteAddr() : forwardedIp);
                    cart.clear();
                    return "redirect:" + paymentUrl;
                } catch (RuntimeException e) {
                    orderDao.failVnpayPayment(orderId);
                    throw e;
                }
            }

            long orderId = orderDao.createCodOrder(account.getUsername(), receiverName.trim(),
                    phone.trim(), address.trim(), note == null ? null : note.trim(), cart);
            cart.clear();
            redirect.addFlashAttribute("success", "Đặt hàng COD thành công. Mã đơn: #" + orderId);
            return "redirect:/orders";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/payment/vnpay-return")
    public String vnpayReturn(@RequestParam Map<String, String> params, Model model) {
        boolean success = false;
        String message;
        Long orderId = parseLong(params.get("vnp_TxnRef"));
        BigDecimal amount = parseVnpayAmount(params.get("vnp_Amount"));

        if (!vnpayService.validateResponse(params)) {
            message = "Chữ ký phản hồi VNPay không hợp lệ. Đơn hàng chưa được xác nhận.";
        } else if (orderId == null || amount == null) {
            message = "Dữ liệu giao dịch VNPay không hợp lệ.";
        } else if ("00".equals(params.get("vnp_ResponseCode"))
                && "00".equals(params.get("vnp_TransactionStatus"))) {
            success = orderDao.completeVnpayPayment(orderId, amount,
                    params.get("vnp_TransactionNo"));
            message = success
                    ? "Thanh toán VNPay thành công. Bạn đã có thể xem video trong lịch sử đơn hàng."
                    : "Không thể đối chiếu đơn hàng hoặc số tiền thanh toán.";
        } else {
            orderDao.failVnpayPayment(orderId);
            message = "Thanh toán VNPay không thành công hoặc đã bị hủy.";
        }

        model.addAttribute("paymentSuccess", success);
        model.addAttribute("paymentMessage", message);
        model.addAttribute("orderId", orderId);
        return "web/payment-result";
    }

    @GetMapping("/payment/vnpay-ipn")
    @ResponseBody
    public Map<String, String> vnpayIpn(@RequestParam Map<String, String> params) {
        Map<String, String> response = new LinkedHashMap<>();
        try {
            if (!vnpayService.validateResponse(params)) {
                return ipnResponse(response, "97", "Invalid Checksum");
            }
            Long orderId = parseLong(params.get("vnp_TxnRef"));
            BigDecimal amount = parseVnpayAmount(params.get("vnp_Amount"));
            if (orderId == null || amount == null) {
                return ipnResponse(response, "01", "Order not found");
            }
            Order_24133058 order = orderDao.findById(orderId);
            if (order == null) return ipnResponse(response, "01", "Order not found");
            if (order.getTotalAmount().compareTo(amount) != 0) {
                return ipnResponse(response, "04", "Invalid Amount");
            }
            if (order.getPaymentStatus() == PaymentStatus_24133058.PAID) {
                return ipnResponse(response, "02", "Order already confirmed");
            }
            if ("00".equals(params.get("vnp_ResponseCode"))
                    && "00".equals(params.get("vnp_TransactionStatus"))) {
                if (!orderDao.completeVnpayPayment(orderId, amount, params.get("vnp_TransactionNo"))) {
                    return ipnResponse(response, "99", "Unknown error");
                }
            } else {
                orderDao.failVnpayPayment(orderId);
            }
            return ipnResponse(response, "00", "Confirm Success");
        } catch (RuntimeException e) {
            return ipnResponse(response, "99", "Unknown error");
        }
    }

    @GetMapping("/orders/{orderId}/watch/{videoId}")
    public String watchPurchasedVideo(@PathVariable long orderId,
                                      @PathVariable String videoId,
                                      HttpSession session,
                                      Model model,
                                      RedirectAttributes redirect) {
        User_24133058 account = getAccount(session);
        if (account == null) return "redirect:/login";
        String videoUrl = orderDao.findPurchasedVideoUrl(orderId, account.getUsername(), videoId);
        if (videoUrl == null) {
            redirect.addFlashAttribute("error",
                    "Video chưa được cấp quyền xem hoặc chưa cấu hình đường dẫn phát.");
            return "redirect:/orders";
        }
        model.addAttribute("video", videoService.findById(videoId));
        model.addAttribute("videoUrl", videoUrl);
        model.addAttribute("orderId", orderId);
        return "web/watch-video";
    }

    @GetMapping("/orders")
    public String orderHistory(@RequestParam(required = false) String status,
                               HttpSession session,
                               Model model) {
        User_24133058 account = getAccount(session);
        if (account == null) return "redirect:/login";

        OrderStatus_24133058 selectedStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                selectedStatus = OrderStatus_24133058.valueOf(status);
            } catch (IllegalArgumentException ignored) {
                // Trạng thái không hợp lệ được xem như bộ lọc "tất cả".
            }
        }
        model.addAttribute("orders", orderDao.findByUsername(account.getUsername(), selectedStatus));
        model.addAttribute("statuses", OrderStatus_24133058.values());
        model.addAttribute("selectedStatus", selectedStatus);
        return "web/order-history";
    }

    private Cart_24133058 getCart(HttpSession session) {
        Cart_24133058 cart = (Cart_24133058) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart_24133058();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private User_24133058 getAccount(HttpSession session) {
        return (User_24133058) session.getAttribute("account");
    }

    private Long parseLong(String value) {
        try { return value == null ? null : Long.valueOf(value); }
        catch (NumberFormatException e) { return null; }
    }

    private BigDecimal parseVnpayAmount(String value) {
        try { return value == null ? null : new BigDecimal(value).movePointLeft(2); }
        catch (NumberFormatException e) { return null; }
    }

    private Map<String, String> ipnResponse(Map<String, String> response,
                                            String code, String message) {
        response.put("RspCode", code);
        response.put("Message", message);
        return response;
    }
}
