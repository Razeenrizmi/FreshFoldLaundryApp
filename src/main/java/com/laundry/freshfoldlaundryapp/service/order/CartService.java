package com.laundry.freshfoldlaundryapp.service.order;

import com.laundry.freshfoldlaundryapp.model.order.CartItem;
import com.laundry.freshfoldlaundryapp.repository.order.CartItemRepository;
import com.laundry.freshfoldlaundryapp.repository.order.ClothesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ClothesRepository clothesRepository;

    // ===== CART CRUD OPERATIONS =====

    // CREATE - Add item to cart
    public CartItem addToCart(Long userId, Integer clothId, Integer quantity, String sessionId) {
        try {
            // Get cloth details
            var clothDetails = clothesRepository.findById(clothId);
            if (clothDetails == null) {
                throw new IllegalArgumentException("Cloth item not found");
            }

            // Check if item already exists in cart
            CartItem existingItem = cartItemRepository.findByUserIdAndClothId(userId, clothId);

            if (existingItem != null) {
                // Update existing item quantity
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.calculateTotalPrice();
                cartItemRepository.updateCartItem(existingItem);
                return existingItem;
            } else {
                // Create new cart item
                CartItem newItem = new CartItem();
                newItem.setUserId(userId);
                newItem.setClothId(clothId);
                newItem.setClothName(clothDetails.getName());
                newItem.setQuantity(quantity);
                newItem.setUnitPrice(clothDetails.getUnitPrice().doubleValue());
                newItem.setCategoryId(clothDetails.getCategoryId());
                newItem.setSessionId(sessionId);
                newItem.calculateTotalPrice();

                Long savedId = cartItemRepository.saveCartItem(newItem);
                newItem.setCartItemId(savedId);
                return newItem;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to add item to cart: " + e.getMessage(), e);
        }
    }

    // READ - Get all cart items for user
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    // READ - Get cart items by session (for guest users)
    public List<CartItem> getCartItemsBySession(String sessionId) {
        return cartItemRepository.findBySessionId(sessionId);
    }

    // READ - Get cart item by ID
    public CartItem getCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    // READ - Get cart summary
    public Map<String, Object> getCartSummary(Long userId) {
        List<CartItem> items = getCartItems(userId);
        Double total = cartItemRepository.getCartTotal(userId);
        Integer itemCount = cartItemRepository.getCartItemCount(userId);

        return Map.of(
            "items", items,
            "total", total,
            "itemCount", itemCount,
            "isEmpty", items.isEmpty()
        );
    }

    // UPDATE - Update cart item quantity
    public boolean updateCartItemQuantity(Long cartItemId, Integer newQuantity) {
        try {
            if (newQuantity <= 0) {
                // Remove item if quantity is 0 or negative
                return removeCartItem(cartItemId);
            }

            CartItem item = getCartItem(cartItemId);
            if (item != null) {
                item.setQuantity(newQuantity);
                item.calculateTotalPrice();
                return cartItemRepository.updateCartItem(item);
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update cart item quantity: " + e.getMessage(), e);
        }
    }

    // UPDATE - Increase quantity
    public boolean increaseQuantity(Long cartItemId) {
        try {
            CartItem item = getCartItem(cartItemId);
            if (item != null) {
                item.incrementQuantity();
                return cartItemRepository.updateCartItem(item);
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to increase quantity: " + e.getMessage(), e);
        }
    }

    // UPDATE - Decrease quantity
    public boolean decreaseQuantity(Long cartItemId) {
        try {
            CartItem item = getCartItem(cartItemId);
            if (item != null) {
                if (item.getQuantity() <= 1) {
                    // Remove item if quantity would become 0
                    return removeCartItem(cartItemId);
                } else {
                    item.decrementQuantity();
                    return cartItemRepository.updateCartItem(item);
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrease quantity: " + e.getMessage(), e);
        }
    }

    // DELETE - Remove specific cart item
    public boolean removeCartItem(Long cartItemId) {
        try {
            return cartItemRepository.deleteCartItem(cartItemId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove cart item: " + e.getMessage(), e);
        }
    }

    // DELETE - Clear entire cart
    public boolean clearCart(Long userId) {
        try {
            return cartItemRepository.clearCartByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear cart: " + e.getMessage(), e);
        }
    }

    // DELETE - Clear cart by session
    public boolean clearCartBySession(String sessionId) {
        try {
            return cartItemRepository.clearCartBySessionId(sessionId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear cart: " + e.getMessage(), e);
        }
    }

    // ===== BUSINESS OPERATIONS =====

    // Get cart total
    public Double getCartTotal(Long userId) {
        return cartItemRepository.getCartTotal(userId);
    }

    // Get cart item count
    public Integer getCartItemCount(Long userId) {
        return cartItemRepository.getCartItemCount(userId);
    }

    // Check if cart is empty
    public boolean isCartEmpty(Long userId) {
        return getCartItemCount(userId) == 0;
    }

    // Validate cart for checkout
    public boolean validateCart(Long userId) {
        try {
            List<CartItem> items = getCartItems(userId);

            if (items.isEmpty()) {
                throw new IllegalArgumentException("Cart is empty");
            }

            // Validate each item
            for (CartItem item : items) {
                if (item.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Invalid quantity for item: " + item.getClothName());
                }
                if (item.getUnitPrice() <= 0) {
                    throw new IllegalArgumentException("Invalid price for item: " + item.getClothName());
                }

                // Check if cloth still exists and is available
                var cloth = clothesRepository.findById(item.getClothId());
                if (cloth == null) {
                    throw new IllegalArgumentException("Item no longer available: " + item.getClothName());
                }
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Cart validation failed: " + e.getMessage(), e);
        }
    }

    // Merge session cart with user cart (when user logs in)
    public boolean mergeSessionCart(String sessionId, Long userId) {
        try {
            List<CartItem> sessionItems = getCartItemsBySession(sessionId);

            for (CartItem sessionItem : sessionItems) {
                // Try to add to user cart
                addToCart(userId, sessionItem.getClothId(), sessionItem.getQuantity(), sessionId);
            }

            // Clear session cart
            clearCartBySession(sessionId);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to merge session cart: " + e.getMessage(), e);
        }
    }

    // Apply discount to cart (if needed for future features)
    public Double applyDiscount(Long userId, String discountCode) {
        // This can be implemented later for discount functionality
        Double originalTotal = getCartTotal(userId);
        // Apply discount logic here
        return originalTotal; // For now, return original total
    }
}
