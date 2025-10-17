//package com.laundry.freshfoldlaundryapp.controller.order;
//
//import com.laundry.freshfoldlaundryapp.model.order.Customer;
//import com.laundry.freshfoldlaundryapp.model.order.OrderItem;
//import com.laundry.freshfoldlaundryapp.model.order.Orders;
//import com.laundry.freshfoldlaundryapp.model.order.SpecialRequest;
//import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//@Controller("customerOrderController")
//public class OrderController {
//
//    @Autowired
//    private OrdersService orderService;
//
//    @GetMapping("/order/orders")
//    public String showOrders(Model model) {
//        List<Orders> current = orderService.getCurrentOrders();
//        List<Orders> completed = orderService.getCompletedOrders();
//        model.addAttribute("currentOrders", current);
//        model.addAttribute("completedOrders", completed);
//        return "order/orders";
//    }
//
//    @GetMapping("/order/form")
//    public String showOrderForm(Model model) {
//        model.addAttribute("customer", new Customer());
//        model.addAttribute("order", new Orders());
//        return "order/orderForm";
//    }
//
//    @PostMapping("/order/save")
//    public String saveOrder(
//            @Valid @ModelAttribute("order") Orders order,
//            BindingResult orderResult,
//            @Valid @ModelAttribute("customer") Customer customer,
//            BindingResult customerResult,
//            @RequestParam(value = "specialRequests", required = false) String specialRequests,
//            HttpSession session,
//            Model model) {
//
//        // Check for validation errors from annotations
//        if (orderResult.hasErrors() || customerResult.hasErrors()) {
//            return "order/orderForm";
//        }
//
//        try {
//            @SuppressWarnings("unchecked")
//            List<OrderItem> items = (List<OrderItem>) session.getAttribute("cart");
//            orderService.saveOrder(customer, order, specialRequests, items);
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "order/orderForm";
//        }
//
//        // Clear cart after save
//        session.removeAttribute("cart");
//
//        return "redirect:/order/success?orderId=" + order.getOrderId();
//    }
//
//
//    @GetMapping("/order/success")
//    public String showSuccess(@RequestParam Integer orderId, Model model) {
//        model.addAttribute("orderId", orderId);
//        return "success";
//    }
//
//    @GetMapping("/order/details/{id}")
//    public String showOrderDetails(@PathVariable Integer id, Model model) {
//        Orders order = orderService.getOrderById(id);
//        if (order == null) {
//            return "redirect:/order/orders"; // Or error page
//        }
//        Customer customer = orderService.getCustomerById(order.getCustomerId());
//        List<SpecialRequest> specialRequests = orderService.getSpecialRequestsByOrderId(id);
//        List<Object[]> orderItems = orderService.getOrderItemsWithDetailsByOrderId(id);
//        BigDecimal totalAmount = orderService.calculateTotalAmount(id);
//
//        model.addAttribute("order", order);
//        model.addAttribute("customer", customer);
//        model.addAttribute("specialRequests", specialRequests);
//        model.addAttribute("orderItems", orderItems);
//        model.addAttribute("totalAmount", totalAmount);
//
//        return "order/orderDetails";
//    }
//
//    @GetMapping("/order/edit/{id}")
//    public String showEditOrder(@PathVariable Integer id, Model model) {
//        Orders order = orderService.getOrderById(id);
//        if (order == null || !"Pending".equals(order.getStatus())) {
//            return "redirect:/order/orders";
//        }
//        Customer customer = orderService.getCustomerById(order.getCustomerId());
//        List<SpecialRequest> specialRequests = orderService.getSpecialRequestsByOrderId(id);
//        List<Object[]> orderItems = orderService.getOrderItemsWithDetailsByOrderId(id);
//        BigDecimal totalAmount = orderService.calculateTotalAmount(id);
//
//        model.addAttribute("order", order);
//        model.addAttribute("customer", customer);
//        model.addAttribute("specialRequests", specialRequests);
//        model.addAttribute("orderItems", orderItems);
//        model.addAttribute("totalAmount", totalAmount);
//
//        return "order/orderEdit";
//    }
//
//    @PostMapping("/order/update/{id}")
//    public String updateOrder(@PathVariable Integer id,
//                              @RequestParam String serviceType,
//                              @RequestParam String name,
//                              @RequestParam String email,
//                              @RequestParam String address,
//                              @RequestParam String phoneNo,
//                              @RequestParam String pickupDatetimeStr,
//                              @RequestParam String deliveryDatetimeStr) {
//        Orders order = orderService.getOrderById(id);
//        Customer customer = orderService.getCustomerById(order.getCustomerId());
//
//        order.setServiceType(serviceType);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
//        order.setPickupDatetime(LocalDateTime.parse(pickupDatetimeStr, formatter));
//        order.setDeliveryDatetime(LocalDateTime.parse(deliveryDatetimeStr, formatter));
//
//        customer.setName(name);
//        customer.setEmail(email);
//        customer.setAddress(address);
//        customer.setPhoneNo(phoneNo);
//
//        orderService.updateOrder(order, customer);
//
//        return "redirect:/order/details/" + id;
//    }
//
//    @PostMapping("/order/update-requests/{id}")
//    public String updateSpecialRequests(@PathVariable Integer id,
//                                        @RequestParam List<String> specialRequests) {
//        orderService.updateSpecialRequests(id, specialRequests);
//        return "redirect:/order/edit/" + id;
//    }
//
//    @GetMapping("/order/delete-request/{id}")
//    public String deleteSpecialRequest(@PathVariable Integer id,
//                                       @RequestParam String name) {
//        orderService.deleteSpecialRequest(id, name);
//        return "redirect:/order/edit/" + id;
//    }
//
//    @GetMapping("/order/delete-item/{id}")
//    public String deleteOrderItem(@PathVariable Integer id,
//                                  @RequestParam Integer clothId) {
//        //orderService.deleteOrderItem(id, clothId);
//        return "redirect:/order/edit/" + id;
//    }
//
//    @PostMapping("/order/update-item-quantity/{id}")
//    public String updateOrderItemQuantity(@PathVariable Integer id,
//                                          @RequestParam Integer clothId,
//                                          @RequestParam Integer quantity) {
//        //orderService.updateOrderItemQuantity(id, clothId, quantity);
//        return "redirect:/order/edit/" + id;
//    }
//
//    @PostMapping("/order/delete/{id}")
//    public String deleteOrder(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
//        //orderService.deleteOrder(id);
//        redirectAttributes.addFlashAttribute("message", "Order successfully deleted");
//        return "redirect:/order/orders";
//    }
//}



package com.laundry.freshfoldlaundryapp.controller.order;

import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.model.order.OrderItem;
import com.laundry.freshfoldlaundryapp.service.CustomUserDetails;
import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
import com.laundry.freshfoldlaundryapp.service.order.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ClothesService clothesService;

    @Autowired
    private OrdersService ordersService;

    // Step 1: Browse clothes by category
    @GetMapping("/browse")
    public String browseClothes(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                Model model, HttpSession session) {
        try {
            // Get all categories for navigation
            model.addAttribute("categories", clothesService.getAllCategories());

            // Always get current cart quantities from session to prevent template errors
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            model.addAttribute("cartQuantities", cart != null ? cart : new HashMap<>());

            if (categoryId != null) {
                // Get clothes in selected category
                model.addAttribute("clothes", clothesService.getClothesByCategory(categoryId));
                model.addAttribute("selectedCategoryId", categoryId);
            }

            return "order/browse";
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load clothes. Please try again.");
            // Ensure cartQuantities is always provided even on error
            model.addAttribute("cartQuantities", new HashMap<>());
            return "order/browse";
        }
    }

    // Step 2: Add items to cart
    @PostMapping("/browse/add")
    public String addToCart(@RequestParam Integer clothId,
                            @RequestParam Integer quantity,
                            @RequestParam Integer categoryId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

            if (cart == null) {
                cart = new HashMap<>();
            }

            if (quantity > 0) {
                cart.put(clothId, quantity);
            } else {
                cart.remove(clothId);
            }

            session.setAttribute("cart", cart);
            redirectAttributes.addAttribute("categoryId", categoryId);
            redirectAttributes.addFlashAttribute("success", "Cart updated successfully!");

            return "redirect:/order/browse";
        } catch (Exception e) {
            redirectAttributes.addAttribute("categoryId", categoryId);
            redirectAttributes.addFlashAttribute("error", "Failed to update cart. Please try again.");
            return "redirect:/order/browse";
        }
    }

    // Step 3: View cart and confirm selection
    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        try {
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

            if (cart == null || cart.isEmpty()) {
                model.addAttribute("cartItems", new ArrayList<>());
                model.addAttribute("totalAmount", 0.0);
                return "order/cart";
            }

            List<OrderItem> cartItems = new ArrayList<>();
            double totalAmount = 0.0;

            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                var cloth = clothesService.getClothById(entry.getKey());
                if (cloth != null && cloth.getName() != null && cloth.getUnitPrice() != null) {
                    OrderItem item = new OrderItem();
                    item.setClothId(entry.getKey());
                    item.setClothName(cloth.getName());
                    item.setQuantity(entry.getValue());
                    // Convert BigDecimal to Double
                    double unitPrice = cloth.getUnitPrice().doubleValue();
                    item.setUnitPrice(unitPrice);
                    item.setTotalPrice(unitPrice * entry.getValue());

                    cartItems.add(item);
                    totalAmount += item.getTotalPrice();
                }
            }

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", totalAmount);
            session.setAttribute("totalAmount", totalAmount);

            return "order/cart";
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load cart. Please try again.");
            model.addAttribute("cartItems", new ArrayList<>());
            model.addAttribute("totalAmount", 0.0);
            return "order/cart";
        }
    }

    // Step 4: Proceed to order form
    @PostMapping("/cart/confirm")
    public String confirmCart(HttpSession session, RedirectAttributes redirectAttributes) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
            return "redirect:/order/cart";
        }

        return "redirect:/order/form";
    }

    // Step 5: Order form with pickup/delivery details
    @GetMapping("/form")
    public String showOrderForm(Model model, HttpSession session, @AuthenticationPrincipal CustomUserDetails userDetails) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/order/browse";
        }

        Orders order = new Orders();
        if (userDetails != null) {
            order.setCustomerId(userDetails.getUserId().intValue());
        }

        model.addAttribute("order", order);
        model.addAttribute("totalAmount", session.getAttribute("totalAmount"));

        return "order/orderForm";
    }

    // Step 6: Process order form and redirect to payment
    @PostMapping("/form/submit")
    public String submitOrderForm(@ModelAttribute Orders order,
                                  HttpSession session,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        try {
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

            if (cart == null || cart.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
                return "redirect:/order/browse";
            }

            // Set order details
            order.setCustomerId(userDetails.getUserId().intValue());
            order.setOrderTime(LocalDateTime.now());
            order.setStatus("Pending Payment");

            // Get cloth types from cart
            StringBuilder clothTypes = new StringBuilder();
            for (Integer clothId : cart.keySet()) {
                var cloth = clothesService.getClothById(clothId);
                if (cloth != null && cloth.getName() != null) {
                    clothTypes.append(cloth.getName()).append(", ");
                }
            }
            if (!clothTypes.isEmpty()) {
                clothTypes.setLength(clothTypes.length() - 2); // Remove last comma
            }
            order.setClothType(clothTypes.toString());

            // Set price from session
            Double totalAmount = (Double) session.getAttribute("totalAmount");
            order.setPrice(totalAmount != null ? totalAmount : 0.0);

            // Save order temporarily in session for payment
            session.setAttribute("pendingOrder", order);
            session.setAttribute("orderCart", cart);

            // Redirect to payment dashboard
            return "redirect:/payment/dashboard";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to process order. Please try again.");
            return "redirect:/order/form";
        }
    }

    // Clear cart after successful payment
    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
        session.removeAttribute("totalAmount");
        session.removeAttribute("pendingOrder");
        session.removeAttribute("orderCart");
    }
}
