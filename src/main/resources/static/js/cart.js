document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('orderForm');
    if (!form) return;

    // Handle order form submission to add items to cart
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const serviceType = document.querySelector('#serviceType')?.value || '';
        const quantity = parseInt(document.querySelector('#quantity')?.value || '1', 10);
        const notes = document.querySelector('#notes')?.value || '';
        const unitPrice = parseFloat(document.querySelector('#unitPrice')?.value || '0');

        if (!serviceType) {
            alert('Please select a Service Type.');
            return;
        }

        try {
            const response = await fetch('/cart/items', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ serviceType, quantity, notes, unitPrice })
            });

            if (!response.ok) {
                throw new Error('Failed to add item to cart');
            }

            const cart = await response.json();
            updateCartDisplay(cart);
            alert('Item added to cart successfully!');

            // Optional: reset form after adding
            // form.reset();

        } catch (error) {
            console.error('Error adding to cart:', error);
            alert('Failed to add item to cart. Please try again.');
        }
    });

    // Load and display cart on page load
    loadCart();

    // Set up cart action handlers
    setupCartActions();
});

async function loadCart() {
    try {
        const response = await fetch('/cart');
        if (response.ok) {
            const cart = await response.json();
            updateCartDisplay(cart);
        }
    } catch (error) {
        console.error('Error loading cart:', error);
    }
}

function updateCartDisplay(cart) {
    const miniCart = document.getElementById('miniCart');
    const cartTotal = document.getElementById('cartTotal');

    if (miniCart) {
        if (cart.items && cart.items.length > 0) {
            miniCart.innerHTML = cart.items.map((item, index) => `
        <li class="cart-item">
          <span>${item.serviceType} x ${item.quantity}</span>
          <span>$${(item.unitPrice * item.quantity).toFixed(2)}</span>
          <button type="button" class="remove-item" data-index="${index}">Remove</button>
        </li>
      `).join('');
        } else {
            miniCart.innerHTML = '<li>Your cart is empty</li>';
        }
    }

    if (cartTotal) {
        cartTotal.textContent = cart.total ? `$${cart.total.toFixed(2)}` : '$0.00';
    }
}

function setupCartActions() {
    // Handle remove item clicks
    document.addEventListener('click', async (e) => {
        if (e.target.classList.contains('remove-item')) {
            const index = e.target.getAttribute('data-index');
            await removeCartItem(index);
        }
    });

    // Handle clear cart button
    const clearCartBtn = document.getElementById('clearCart');
    if (clearCartBtn) {
        clearCartBtn.addEventListener('click', async () => {
            if (confirm('Are you sure you want to clear your cart?')) {
                await clearCart();
            }
        });
    }
}

async function removeCartItem(index) {
    try {
        const response = await fetch(`/cart/items/${index}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            const cart = await response.json();
            updateCartDisplay(cart);
        } else {
            throw new Error('Failed to remove item');
        }
    } catch (error) {
        console.error('Error removing item:', error);
        alert('Failed to remove item from cart.');
    }
}

async function clearCart() {
    try {
        const response = await fetch('/cart/clear', {
            method: 'POST'
        });

        if (response.ok) {
            updateCartDisplay({ items: [], total: 0 });
            alert('Cart cleared successfully!');
        } else {
            throw new Error('Failed to clear cart');
        }
    } catch (error) {
        console.error('Error clearing cart:', error);
        alert('Failed to clear cart.');
    }
}
