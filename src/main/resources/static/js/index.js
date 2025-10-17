// Cart logic for index.html
let cart = JSON.parse(localStorage.getItem('laundryCart')) || [];

function saveCart() {
    localStorage.setItem('laundryCart', JSON.stringify(cart));
}

function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    const cartCount = document.getElementById('cartCount');
    if (cartCount) {
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        cartCount.textContent = totalItems;
        cartCount.style.display = totalItems > 0 ? 'flex' : 'none';
    }
}

function updateCartDisplay() {
    const cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    const cartItems = document.getElementById('cartItems');
    const cartItemsCount = document.getElementById('cartItemsCount');
    const cartTotal = document.getElementById('cartTotal');

    if (cart.length === 0) {
        cartItems.innerHTML = `<div class='cart-empty'><i class='fas fa-shopping-basket' style='font-size:48px;color:#ddd;margin-bottom:15px;'></i><p>Your washing basket is empty</p></div>`;
        cartItemsCount.textContent = '0 items in your basket';
        cartTotal.textContent = '0.00';
    } else {
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        const totalPrice = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
        cartItemsCount.textContent = `${totalItems} item${totalItems !== 1 ? 's' : ''} in your basket`;
        cartTotal.textContent = totalPrice.toFixed(2);
        cartItems.innerHTML = cart.map((item, index) => `
            <div class='cart-item'>
                <img src='${item.imageUrl}' alt='${item.clothName}' class='cart-item-image' onerror="this.src='/images/WashnFold.jpg'">
                <div class='cart-item-details'>
                    <div class='cart-item-name'>${item.clothName}</div>
                    <div class='cart-item-service'>${item.serviceType} | ${item.deliveryForm}</div>
                </div>
                <div class='cart-item-price'>Rs ${item.price.toFixed(2)}</div>
                <div class='cart-item-quantity'>
                    <button class='quantity-btn' onclick='updateQuantity(${index}, -1)'>−</button>
                    <span class='quantity-display'>${item.quantity}</span>
                    <button class='quantity-btn' onclick='updateQuantity(${index}, 1)'>+</button>
                </div>
                <button class='cart-item-remove' onclick='removeFromCart(${index})'>×</button>
            </div>
        `).join('');
    }
}

function addToCart(serviceId, serviceName, price, imageUrl, services) {
    const existingItem = cart.find(item => item.serviceId === serviceId);
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({ serviceId, serviceName, price, quantity: 1, imageUrl, services });
    }
    saveCart();
    updateCartDisplay();
    updateCartCount();
    showCartNotification('Item added to cart!');
}

function removeFromCart(index) {
    let cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    cart.splice(index, 1);
    localStorage.setItem('laundryCart', JSON.stringify(cart));
    updateCartDisplay();
    updateCartCount();
}

function updateQuantity(index, change) {
    let cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    if (cart[index]) {
        cart[index].quantity += change;
        if (cart[index].quantity <= 0) {
            removeFromCart(index);
        } else {
            localStorage.setItem('laundryCart', JSON.stringify(cart));
            updateCartDisplay();
            updateCartCount();
        }
    }
}

function toggleCart() {
    const cartDropdown = document.getElementById('cartDropdown');
    cartDropdown.style.display = cartDropdown.style.display === 'block' ? 'none' : 'block';
    updateCartDisplay();
}

function closeCart() {
    document.getElementById('cartDropdown').style.display = 'none';
}

// Close cart when clicking outside
window.addEventListener('click', function(event) {
    const cartDropdown = document.getElementById('cartDropdown');
    if (cartDropdown.style.display === 'block' && !cartDropdown.contains(event.target) && !event.target.classList.contains('cart-btn') && !event.target.classList.contains('wash-bag-btn')) {
        cartDropdown.style.display = 'none';
    }
});

document.addEventListener('DOMContentLoaded', function() {
    updateCartDisplay();
    updateCartCount();
});

// Call updateCartCount and updateCartDisplay on page load
window.onload = function() {
    updateCartCount();
    updateCartDisplay();
};

function showCartNotification(message) {
    const toast = document.createElement('div');
    toast.style.cssText = `position:fixed;top:20px;right:20px;background:#4CAF50;color:white;padding:15px 20px;border-radius:8px;z-index:1001;animation:slideInRight 0.3s ease;`;
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => { toast.remove(); }, 2000);
}

function checkout() {
    const cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    if (cart.length === 0) {
        alert('Your cart is empty!');
        return;
    }
    // Redirect to payment page
    window.location.href = '/payment';
}

function redirectToClothes(serviceType) {
    window.location.href = '/orders/select-clothes?serviceType=' + encodeURIComponent(serviceType);
}
