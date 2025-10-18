//package com.laundry.freshfoldlaundryapp.controller.payment;
//
//import com.laundry.freshfoldlaundryapp.dto.payment.PaymentDto;
//import com.laundry.freshfoldlaundryapp.model.payment.Payment;
//import com.laundry.freshfoldlaundryapp.service.payment.PaymentService;
//import jakarta.validation.Valid;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.math.BigDecimal;
//
//@Controller
//@RequestMapping("/payments")
//public class PaymentController {
//
//    private final PaymentService paymentService;
//
//    public PaymentController(PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }
//
//    @GetMapping("/form")
//    public String showForm(Model model) {
//        model.addAttribute("paymentDto", new PaymentDto());
//        return "payment-form";
//    }
//
//    @PostMapping("/submit")
//    public String submitPayment(@Valid @ModelAttribute PaymentDto paymentDto,
//                                BindingResult result,
//                                Model model) {
//        if (result.hasErrors()) {
//            return "payment-form";
//        }
//        paymentService.processPayment(paymentDto);
//        return "redirect:/payments/success";
//    }
//
//    @GetMapping("/success")
//    public String successPage() {
//        return "payment-success";
//    }
//
//    @GetMapping("/list")
//    public String listPayments(Model model) {
//        model.addAttribute("payments", paymentService.getAllPayments());
//        return "payment-list";
//    }
//
//    @GetMapping("/payment/card")
//    public String showCardPaymentPage(@RequestParam int orderId,
//                                      @RequestParam BigDecimal amount,
//                                      Model model) {
//        model.addAttribute("orderId", orderId);
//        model.addAttribute("amount", amount);
//        return "payment/card-payment";
//    }
//
//    @PostMapping("/payments/pay/card")
//    public String processCardDetails(@RequestParam String cardholderName,
//                                     @RequestParam String cardNumber,
//                                     @RequestParam String expiryDate,
//                                     @RequestParam String cvv,
//                                     @RequestParam int orderId,
//                                     @RequestParam BigDecimal amount,
//                                     Model model) {
//        model.addAttribute("orderId", orderId);
//        model.addAttribute("cardholderName", cardholderName);
//        model.addAttribute("amount", amount);
//        return "/otp-verification";
//    }
//
//    @PostMapping("/pay/card/otp")
//    public String verifyOtp(@RequestParam String otp,
//                            @RequestParam int orderId,
//                            @RequestParam BigDecimal amount,
//                            RedirectAttributes redirectAttributes) {
//
//        paymentService.recordPayment(orderId, amount, "Card", "Paid");
//
//        redirectAttributes.addAttribute("orderId", orderId);
//        redirectAttributes.addAttribute("otp", otp);
//        return "redirect:/payments/receipt";
//    }
//
//    @GetMapping("/receipt")
//    public String showReceipt(@RequestParam int orderId,
//                              @RequestParam(required = false) String otp,
//                              Model model) {
//        model.addAttribute("orderId", orderId);
//        model.addAttribute("otp", otp);
//        return "payment/receipt";
//    }
//
//    @PostMapping("/download")
//    public ResponseEntity<String> downloadReceipt(@RequestParam int orderId) {
//        Payment payment = paymentService.getPaymentByOrderId(orderId);
//        String receipt = "Receipt for Order ID: " + orderId +
//                "\nStatus: " + payment.getPaymentStatus() +
//                "\nAmount: LKR " + payment.getAmount();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt_" + orderId + ".txt")
//                .contentType(MediaType.TEXT_PLAIN)
//                .body(receipt);
//    }
//
//    @GetMapping("/payment/cod")
//    public String showCODPage(@RequestParam int orderId,
//                              @RequestParam BigDecimal amount,
//                              Model model) {
//        model.addAttribute("orderId", orderId);
//        model.addAttribute("amount", amount);
//        return "payment/cod-payment";
//    }
//
//    @PostMapping("/pay/cod")
//    public String confirmCOD(@RequestParam int orderId,
//                             @RequestParam BigDecimal amount,
//                             RedirectAttributes redirectAttributes) {
//
//        paymentService.recordPayment(orderId, amount, "CashOnDelivery", "Pending");
//
//        redirectAttributes.addAttribute("orderId", orderId);
//        return "redirect:/receipt";
//    }
//
//    @GetMapping("/payment/dashboard")
//    public String showDashboard(@RequestParam(required = false) Integer orderId, Model model) {
//        Payment payment;
//
//        if (orderId != null) {
//            payment = paymentService.getPaymentByOrderId(orderId);
//        } else {
//            payment = new Payment();
//            payment.setOrderId(1001);
//            payment.setAmount(new BigDecimal("250.00"));
//            payment.setPaymentStatus("Pending");
//            payment.setPaymentId(1);
//        }
//
//        model.addAttribute("payment", payment);
//        return "payment/dashboard";
//    }
//
//    @PostMapping("/cancel")
//    public String cancelPayment(@RequestParam int orderId,
//                                @RequestParam BigDecimal amount,
//                                RedirectAttributes redirectAttributes) {
//
//        paymentService.recordPayment(orderId, amount, "CashOnDelivery", "Cancelled");
//
//        return "redirect:/payments/dashboard?orderId=" + orderId;
//    }
//}

package com.laundry.freshfoldlaundryapp.controller.payment;

import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.model.order.Customer;
import com.laundry.freshfoldlaundryapp.model.payment.Payment;
import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
import com.laundry.freshfoldlaundryapp.service.CustomUserDetails;
import com.laundry.freshfoldlaundryapp.repository.order.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private CustomerRepository customerRepository;

    // Payment Dashboard - shows order summary and payment options
    @GetMapping("/dashboard")
    public String showPaymentDashboard(Model model, HttpSession session) {
        // Get pending order from session
        Orders pendingOrder = (Orders) session.getAttribute("pendingOrder");

        if (pendingOrder == null) {
            model.addAttribute("error", "No pending order found. Please create an order first.");
            return "redirect:/order/browse";
        }

        // Get order details for display
        model.addAttribute("order", pendingOrder);
        model.addAttribute("amount", pendingOrder.getPrice());

        return "payment/dashboard";
    }

    // Card Payment Form - can be accessed directly or with amount parameter
    @GetMapping("/card")
    public String showCardPaymentForm(@RequestParam(required = false) String amount,
                                    Model model, HttpSession session) {
        try {
            // If no amount provided, try to get from pending order
            if (amount == null || amount.isEmpty()) {
                Orders pendingOrder = (Orders) session.getAttribute("pendingOrder");
                if (pendingOrder != null && pendingOrder.getPrice() != null) {
                    amount = pendingOrder.getPrice().toString();
                } else {
                    // Default amount for direct access testing
                    amount = "100.00";
                }
            }

            model.addAttribute("amount", amount);

            // Add order details if available
            Orders pendingOrder = (Orders) session.getAttribute("pendingOrder");
            if (pendingOrder != null) {
                model.addAttribute("order", pendingOrder);
            }

            return "payment/card-payment";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading card payment form: " + e.getMessage());
            return "redirect:/payment/dashboard";
        }
    }

    // Process card payment
    @PostMapping("/card/process")
    public String processCardPayment(@RequestParam String cardNumber,
                                   @RequestParam String cvv,
                                   @RequestParam String cardholderName,
                                   @RequestParam String expiryDate,
                                   @RequestParam String amount,
                                   HttpSession session,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Basic card validation
            if (cardNumber == null || cardNumber.trim().length() < 16) {
                redirectAttributes.addFlashAttribute("error", "Invalid card number. Please enter a valid 16-digit card number.");
                return "redirect:/payment/card?amount=" + amount;
            }

            if (cvv == null || cvv.trim().length() < 3) {
                redirectAttributes.addFlashAttribute("error", "Invalid CVV. Please enter a valid 3-digit CVV.");
                return "redirect:/payment/card?amount=" + amount;
            }

            if (cardholderName == null || cardholderName.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please enter the cardholder name.");
                return "redirect:/payment/card?amount=" + amount;
            }

            if (expiryDate == null || expiryDate.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please enter the expiry date.");
                return "redirect:/payment/card?amount=" + amount;
            }

            // Get pending order from session
            Orders pendingOrder = (Orders) session.getAttribute("pendingOrder");
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> orderCart = (Map<Integer, Integer>) session.getAttribute("orderCart");

            if (pendingOrder == null) {
                redirectAttributes.addFlashAttribute("error", "No order found. Please create an order first.");
                return "redirect:/order/browse";
            }

            // Create or get Customer record to satisfy foreign key constraint
            Integer customerId = ensureCustomerExists(userDetails);
            if (customerId == null) {
                redirectAttributes.addFlashAttribute("error", "Failed to create customer record. Please try again.");
                return "redirect:/payment/card?amount=" + amount;
            }

            // Update order with valid customer_id from Customer table
            pendingOrder.setCustomerId(customerId);
            pendingOrder.setPrice(Double.parseDouble(amount));
            pendingOrder.setStatus("Confirmed");
            pendingOrder.setOrderTime(LocalDateTime.now());

            // Save the order - handle return type properly
            Orders savedOrder = null;
            Integer savedOrderId = null;

            try {
                System.out.println("=== CONTROLLER: About to save order ===");
                System.out.println("Order details:");
                System.out.println("  Customer ID: " + pendingOrder.getCustomerId());
                System.out.println("  Service Type: " + pendingOrder.getServiceType());
                System.out.println("  Pickup Datetime: " + pendingOrder.getPickupDatetime());
                System.out.println("  Delivery Datetime: " + pendingOrder.getDeliveryDatetime());
                System.out.println("  Special Instructions: " + pendingOrder.getSpecialInstructions());
                System.out.println("  Price: " + pendingOrder.getPrice());
                System.out.println("  Status: " + pendingOrder.getStatus());

                if (orderCart != null) {
                    System.out.println("Saving order with items (cart size: " + orderCart.size() + ")...");
                    // Try saveOrderWithItems first
                    savedOrder = ordersService.saveOrderWithItems(pendingOrder, orderCart);
                    System.out.println("✓ Order saved with items. Order ID: " + savedOrder.getOrderId());
                } else {
                    System.out.println("Saving order without cart items...");
                    // Try saveOrder method
                    savedOrderId = ordersService.saveOrder(pendingOrder);
                    if (savedOrderId != null) {
                        pendingOrder.setOrderId(savedOrderId);
                        savedOrder = pendingOrder;
                        System.out.println("✓ Order saved. Order ID: " + savedOrderId);
                    } else {
                        System.err.println("✗ Order save returned null!");
                    }
                }
            } catch (Exception e) {
                System.err.println("=== ORDER SAVE ERROR ===");
                System.err.println("Error saving order: " + e.getMessage());
                System.err.println("Error class: " + e.getClass().getName());
                e.printStackTrace();
                System.err.println("=========================");

                // Fallback to simple save
                try {
                    System.out.println("Trying fallback order save...");
                    savedOrderId = ordersService.saveOrder(pendingOrder);
                    if (savedOrderId != null) {
                        pendingOrder.setOrderId(savedOrderId);
                        savedOrder = pendingOrder;
                        System.out.println("✓ Fallback order save successful. Order ID: " + savedOrderId);
                    } else {
                        System.err.println("✗ Fallback order save also returned null!");
                    }
                } catch (Exception e2) {
                    System.err.println("✗ Fallback order save also failed: " + e2.getMessage());
                    throw e2;
                }
            }

            // Create payment record
            if (savedOrder != null && savedOrder.getOrderId() != null) {
                Payment payment = new Payment();
                payment.setOrderId(savedOrder.getOrderId());
                payment.setAmount(Double.parseDouble(amount));
                payment.setPaymentMethod("Card");
                payment.setPaymentTime(LocalDateTime.now());
                payment.setStatus("Completed");

                // Set all card details including the new fields
                String maskedCardNumber = "**** **** **** " + cardNumber.substring(Math.max(0, cardNumber.length() - 4));
                payment.setCardNumber(maskedCardNumber);
                payment.setCardHolder(cardholderName);
                payment.setExpiryDate(expiryDate);
                payment.setCvv("***"); // Don't store actual CVV for security

                // Save payment
                try {
                    System.out.println("=== CONTROLLER: About to save payment ===");
                    System.out.println("Order ID: " + savedOrder.getOrderId());
                    System.out.println("Amount: " + amount);
                    System.out.println("Payment Method: Card");
                    System.out.println("Payment object created, calling ordersService.savePayment()...");
                    ordersService.savePayment(payment);
                    System.out.println("=== CONTROLLER: Payment saved successfully ===");
                } catch (Exception e) {
                    // Payment save failed, but order was saved
                    System.err.println("=== CONTROLLER: Payment save failed ===");
                    System.err.println("Error: " + e.getMessage());
                    System.err.println("Error class: " + e.getClass().getName());
                    e.printStackTrace();
                    System.err.println("=== End of controller error ===");
                    throw new RuntimeException("Error processing payment: " + e.getMessage(), e);
                }

                // Clear session data
                session.removeAttribute("cart");
                session.removeAttribute("pendingOrder");
                session.removeAttribute("orderCart");

                redirectAttributes.addFlashAttribute("success", "Payment successful! Your order has been placed.");
                redirectAttributes.addFlashAttribute("orderId", savedOrder.getOrderId());
                redirectAttributes.addFlashAttribute("amount", amount);

                return "redirect:/payment/success";
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to save order. Please try again.");
                return "redirect:/payment/card?amount=" + amount;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Payment failed: " + e.getMessage());
            return "redirect:/payment/card?amount=" + amount;
        }
    }

    // Process other payment methods (Cash on Delivery)
    @PostMapping("/process")
    public String processPayment(@RequestParam String paymentMethod,
                               HttpSession session,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            Orders pendingOrder = (Orders) session.getAttribute("pendingOrder");
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> orderCart = (Map<Integer, Integer>) session.getAttribute("orderCart");

            if (pendingOrder == null) {
                redirectAttributes.addFlashAttribute("error", "No order found. Please create an order first.");
                return "redirect:/order/browse";
            }

            if ("cash".equals(paymentMethod)) {
                // Create or get Customer record to satisfy foreign key constraint
                Integer customerId = ensureCustomerExists(userDetails);
                if (customerId == null) {
                    redirectAttributes.addFlashAttribute("error", "Failed to create customer record. Please try again.");
                    return "redirect:/payment/dashboard";
                }

                // Update order with valid customer_id from Customer table
                pendingOrder.setCustomerId(customerId);
                pendingOrder.setStatus("Pending Payment");
                pendingOrder.setOrderTime(LocalDateTime.now());

                // Save the order - handle return type properly
                Orders savedOrder = null;
                Integer savedOrderId = null;

                try {
                    if (orderCart != null) {
                        // Try saveOrderWithItems first
                        savedOrder = ordersService.saveOrderWithItems(pendingOrder, orderCart);
                    } else {
                        // Try saveOrder method
                        savedOrderId = ordersService.saveOrder(pendingOrder);
                        if (savedOrderId != null) {
                            pendingOrder.setOrderId(savedOrderId);
                            savedOrder = pendingOrder;
                        }
                    }
                } catch (Exception e) {
                    // Fallback to simple save
                    savedOrderId = ordersService.saveOrder(pendingOrder);
                    if (savedOrderId != null) {
                        pendingOrder.setOrderId(savedOrderId);
                        savedOrder = pendingOrder;
                    }
                }

                // Create payment record for COD
                if (savedOrder != null && savedOrder.getOrderId() != null) {
                    Payment payment = new Payment();
                    payment.setOrderId(savedOrder.getOrderId());
                    payment.setAmount(pendingOrder.getPrice());
                    payment.setPaymentMethod("Cash on Delivery");
                    payment.setPaymentTime(LocalDateTime.now());
                    payment.setStatus("Pending");

                    try {
                        ordersService.savePayment(payment);
                    } catch (Exception e) {
                        // Payment save failed, but order was saved
                        System.err.println("Payment save failed: " + e.getMessage());
                    }

                    // Clear session data
                    session.removeAttribute("cart");
                    session.removeAttribute("pendingOrder");
                    session.removeAttribute("orderCart");

                    redirectAttributes.addFlashAttribute("success", "Order placed successfully! You can pay cash on delivery.");
                    redirectAttributes.addFlashAttribute("orderId", savedOrder.getOrderId());

                    return "redirect:/payment/success";
                } else {
                    redirectAttributes.addFlashAttribute("error", "Failed to save order. Please try again.");
                    return "redirect:/payment/dashboard";
                }
            }

            redirectAttributes.addFlashAttribute("error", "Invalid payment method selected.");
            return "redirect:/payment/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error processing payment: " + e.getMessage());
            return "redirect:/payment/dashboard";
        }
    }

    /**
     * Ensures a Customer record exists for the authenticated user.
     * If the customer doesn't exist, creates a new one.
     *
     * @param userDetails The authenticated user details
     * @return The customer_id from the Customer table, or null if failed
     */
    private Integer ensureCustomerExists(CustomUserDetails userDetails) {
        try {
            if (userDetails == null || userDetails.getUsername() == null) {
                return null;
            }

            // Try to find existing customer by email
            Customer existingCustomer = customerRepository.findByEmail(userDetails.getUsername());

            if (existingCustomer != null) {
                return existingCustomer.getCustomerId();
            }

            // Create new customer record with available data
            Customer newCustomer = new Customer();
            newCustomer.setEmail(userDetails.getUsername());

            // Use username as name (will be updated when user provides more info)
            String username = userDetails.getUsername();
            String displayName = username.contains("@") ? username.substring(0, username.indexOf("@")) : username;
            newCustomer.setName(displayName);

            // Set default values for phone and address (user can update later)
            newCustomer.setPhoneNo("N/A"); // Will be updated on profile completion
            newCustomer.setAddress("N/A"); // Will be updated on profile completion

            // Save and return the new customer_id
            return customerRepository.save(newCustomer);

        } catch (Exception e) {
            System.err.println("Error ensuring customer exists: " + e.getMessage());
            return null;
        }
    }

    // Payment success page
    @GetMapping("/success")
    public String showPaymentSuccess(@RequestParam(required = false) Integer orderId, Model model) {
        if (orderId != null) {
            model.addAttribute("orderId", orderId);
        }
        return "payment/success";
    }
}
