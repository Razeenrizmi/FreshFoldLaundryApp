package com.laundry.freshfoldlaundryapp.controller.order;

import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.service.CustomUserDetails;
import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
import com.laundry.freshfoldlaundryapp.service.order.ClothesService;
import com.laundry.freshfoldlaundryapp.service.order.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
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

    @Autowired
    private CartService cartService;

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
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            if (userDetails != null) {
                // For authenticated users, use CartService to save to database
                Long userId = userDetails.getUserId();
                String sessionId = session.getId();

                // Get cloth details for the cart item
                var cloth = clothesService.getClothById(clothId);
                if (cloth != null) {
                    if (quantity > 0) {
                        // Add or update item in database
                        cartService.addToCart(userId, clothId, quantity, sessionId);
                    } else {
                        // Remove item from database if quantity is 0
                        // We need to find the cart item ID first, then remove it
                        var cartItems = cartService.getCartItems(userId);
                        for (var item : cartItems) {
                            if (item.getClothId().equals(clothId)) {
                                cartService.removeCartItem(item.getCartItemId());
                                break;
                            }
                        }
                    }
                }
            } else {
                // For guest users, use session storage as fallback
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
            }

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
    public String viewCart(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails != null) {
                Long userId = userDetails.getUserId();
                Map<String, Object> cartSummary = cartService.getCartSummary(userId);
                model.addAttribute("cartSummary", cartSummary);
                model.addAttribute("cartItems", cartSummary.get("items"));
                model.addAttribute("totalAmount", cartSummary.get("totalAmount"));
            } else {
                // Handle guest cart using session - fallback to empty cart for now
                model.addAttribute("cartItems", List.of());
                model.addAttribute("totalAmount", 0.0);
            }

            return "order/cart";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to load cart: " + e.getMessage());
            model.addAttribute("cartItems", List.of());
            model.addAttribute("totalAmount", 0.0);
            return "order/cart";
        }
    }

    // AJAX-compatible cart endpoints that return JSON responses
    @PostMapping("/cart/increase/{cartItemId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> increaseCartItemQuantity(@PathVariable Long cartItemId,
                                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userDetails != null) {
                boolean updated = cartService.increaseQuantity(cartItemId);
                if (updated) {
                    response.put("success", true);
                    response.put("message", "Quantity increased");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Failed to increase quantity");
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "Authentication required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/cart/decrease/{cartItemId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> decreaseCartItemQuantity(@PathVariable Long cartItemId,
                                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userDetails != null) {
                boolean updated = cartService.decreaseQuantity(cartItemId);
                if (updated) {
                    response.put("success", true);
                    response.put("message", "Quantity decreased");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Failed to decrease quantity");
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "Authentication required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/cart/remove/{cartItemId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeCartItem(@PathVariable Long cartItemId,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userDetails != null) {
                boolean removed = cartService.removeCartItem(cartItemId);
                if (removed) {
                    response.put("success", true);
                    response.put("message", "Item removed from cart");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Failed to remove item");
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "Authentication required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/cart/clear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearUserCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userDetails != null) {
                boolean cleared = cartService.clearCart(userDetails.getUserId());
                if (cleared) {
                    response.put("success", true);
                    response.put("message", "Cart cleared successfully");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Failed to clear cart");
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "Authentication required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Step 4: Proceed to order form
    @PostMapping("/cart/confirm")
    public String confirmCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            if (userDetails != null) {
                // For authenticated users, check database cart
                Long userId = userDetails.getUserId();
                var cartItems = cartService.getCartItems(userId);

                if (cartItems == null || cartItems.isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
                    return "redirect:/order/cart";
                }

                // Store cart data for order processing
                session.setAttribute("cartItems", cartItems);

                // Calculate total amount and store in session
                Map<String, Object> cartSummary = cartService.getCartSummary(userId);
                Double totalAmount = (Double) cartSummary.get("total");

                // Ensure totalAmount is not null
                if (totalAmount == null) {
                    totalAmount = 0.0;
                    // Calculate manually if service returns null
                    for (var item : cartItems) {
                        if (item.getTotalPrice() != null) {
                            totalAmount += item.getTotalPrice();
                        }
                    }
                }

                session.setAttribute("totalAmount", totalAmount);
                System.out.println("=== Cart Confirmation Debug ===");
                System.out.println("User ID: " + userId);
                System.out.println("Cart items count: " + cartItems.size());
                System.out.println("Total amount calculated: " + totalAmount);
                System.out.println("==============================");

                return "redirect:/order/form";
            } else {
                // For guest users, check session cart
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

                if (cart == null || cart.isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
                    return "redirect:/order/cart";
                }

                // Calculate total for guest users
                double totalAmount = 0.0;
                for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                    Integer clothId = entry.getKey();
                    Integer quantity = entry.getValue();

                    try {
                        var cloth = clothesService.getClothById(clothId);
                        if (cloth != null && cloth.getUnitPrice() != null) {
                            totalAmount += cloth.getUnitPrice().doubleValue() * quantity;
                        }
                    } catch (Exception e) {
                        System.err.println("Error calculating price for cloth ID " + clothId + ": " + e.getMessage());
                    }
                }

                session.setAttribute("totalAmount", totalAmount);
                System.out.println("=== Guest Cart Confirmation Debug ===");
                System.out.println("Cart items count: " + cart.size());
                System.out.println("Total amount calculated: " + totalAmount);
                System.out.println("=====================================");

                return "redirect:/order/form";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to process cart. Please try again.");
            return "redirect:/order/cart";
        }
    }

    // Step 5: Order form with pickup/delivery details
    @GetMapping("/form")
    public String showOrderForm(Model model, HttpSession session, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails != null) {
                // For authenticated users, check if we have cart items from confirmation
                var cartItems = session.getAttribute("cartItems");
                if (cartItems == null) {
                    return "redirect:/order/cart";
                }
            } else {
                // For guest users, check session cart
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
                if (cart == null || cart.isEmpty()) {
                    return "redirect:/order/cart";
                }
            }

            Orders order = new Orders();
            if (userDetails != null) {
                order.setCustomerId(userDetails.getUserId().intValue());
            }

            model.addAttribute("order", order);
            model.addAttribute("totalAmount", session.getAttribute("totalAmount"));

            return "order/orderForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to load order form: " + e.getMessage());
            return "redirect:/order/cart";
        }
    }

    // Step 6: Process order form and redirect to payment
    @PostMapping("/form/submit")
    public String submitOrderForm(@ModelAttribute Orders order,
                                  @RequestParam String customerName,
                                  @RequestParam String customerEmail,
                                  @RequestParam String customerAddress,
                                  @RequestParam String customerPhone,
                                  HttpSession session,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (userDetails != null) {
                // For authenticated users, get cart items from session
                var cartItems = session.getAttribute("cartItems");
                if (cartItems == null) {
                    redirectAttributes.addFlashAttribute("error", "Your cart session has expired!");
                    return "redirect:/order/cart";
                }

                // Set order details
                order.setCustomerId(userDetails.getUserId().intValue());
                order.setOrderTime(LocalDateTime.now());
                order.setStatus("Pending Payment");

                // Get cloth types from cart items for authenticated users
                StringBuilder clothTypes = new StringBuilder();
                try {
                    // Alternative: Get cloth types from database cart
                    var userCartItems = cartService.getCartItems(userDetails.getUserId());
                    if (userCartItems != null) {
                        for (var cartItem : userCartItems) {
                            var cloth = clothesService.getClothById(cartItem.getClothId());
                            if (cloth != null && cloth.getName() != null) {
                                clothTypes.append(cloth.getName()).append(", ");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error getting cloth types for authenticated user: " + e.getMessage());
                }

                if (!clothTypes.isEmpty()) {
                    clothTypes.setLength(clothTypes.length() - 2); // Remove last comma
                    order.setClothType(clothTypes.toString());
                } else {
                    order.setClothType("Mixed Items"); // Fallback
                }

                // Set price from session
                Double totalAmount = (Double) session.getAttribute("totalAmount");
                order.setPrice(totalAmount != null ? totalAmount : 0.0);

                // Store customer details in session for payment processing
                Map<String, String> customerDetails = new HashMap<>();
                customerDetails.put("name", customerName);
                customerDetails.put("email", customerEmail);
                customerDetails.put("address", customerAddress);
                customerDetails.put("phone", customerPhone);
                session.setAttribute("customerDetails", customerDetails);

                // Save order temporarily in session for payment
                session.setAttribute("pendingOrder", order);

                // Redirect to payment dashboard
                return "redirect:/payment/dashboard";
            } else {
                // For guest users, check session cart
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

                if (cart == null || cart.isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
                    return "redirect:/order/browse";
                }

                // Handle guest order processing
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

                // Store customer details and order in session for payment
                Map<String, String> customerDetails = new HashMap<>();
                customerDetails.put("name", customerName);
                customerDetails.put("email", customerEmail);
                customerDetails.put("address", customerAddress);
                customerDetails.put("phone", customerPhone);
                session.setAttribute("customerDetails", customerDetails);
                session.setAttribute("pendingOrder", order);
                session.setAttribute("orderCart", cart);

                // Redirect to payment dashboard
                return "redirect:/payment/dashboard";
            }

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
