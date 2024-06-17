//package com.paymentback.paymentback.Controller;
//
//import com.paymentback.paymentback.services.OrderInfoService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/payment")
//public class PaymentNotificationController {
//
//    private final OrderInfoService orderInfoService;
//
//    @Value("${merchant.secret}")
//    private String merchantSecret;
//
//    @Value("${merchant.id}")
//    private String merchantId;
//
//    public PaymentNotificationController(OrderInfoService orderInfoService) {
//        this.orderInfoService = orderInfoService;
//    }
//
//    @PostMapping("/notify")
//    public ResponseEntity<String> paymentNotify(@RequestParam Map<String, String> payload) {
//        String merchant_id = payload.get("merchant_id");
//        String order_id = payload.get("order_id");
//        String payhere_amount = payload.get("payhere_amount");
//        String payhere_currency = payload.get("payhere_currency");
//        String status_code = payload.get("status_code");
//        String md5sig = payload.get("md5sig");
//
//
//
//        // Generate local MD5 signature
//        String local_md5sig = generateMd5Sig(merchant_id, order_id, payhere_amount, payhere_currency, status_code, merchantSecret);
//
//        if (local_md5sig.equals(md5sig)) {
//            if ("2".equals(status_code)) {
//                // Update the order status to "Successful"
//                orderInfoService.updatePaymentStatus(order_id, "Successful");
//                return ResponseEntity.ok("Payment status updated to Successful");
//            } else {
//                // Update the order status to "Failed"
//                orderInfoService.updatePaymentStatus(order_id, "Failed");
//                return ResponseEntity.ok("Payment status updated to Failed");
//            }
//        } else {
//            return ResponseEntity.status(400).body("Invalid payment notification");
//        }
//    }
//
//    private String generateMd5Sig(String merchant_id, String order_id, String payhere_amount, String payhere_currency, String status_code, String merchant_secret) {
//        String data = merchant_id + order_id + payhere_amount + payhere_currency + status_code + getMd5(merchant_secret).toUpperCase();
//        return getMd5(data).toUpperCase();
//    }
//
//    private String getMd5(String input) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] messageDigest = md.digest(input.getBytes());
//            BigInteger no = new BigInteger(1, messageDigest);
//            StringBuilder hashtext = new StringBuilder(no.toString(16));
//            while (hashtext.length() < 32) {
//                hashtext.insert(0, "0");
//            }
//            return hashtext.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
