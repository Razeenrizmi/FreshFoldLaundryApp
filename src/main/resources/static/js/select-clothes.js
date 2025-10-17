// Utility to get query param
function getQueryParam(name) {
    const url = new URL(window.location.href);
    return url.searchParams.get(name) || '';
}

// Modal state
let modalCloth = {
    name: '',
    img: '',
    price: 0,
    qty: 1,
    notes: '',
    serviceType: '',
    pickupSlot: '',
    deliverySlot: ''
};

function openCheckoutModal(name, img, price) {
    modalCloth.name = name;
    modalCloth.img = img;
    modalCloth.price = price;
    modalCloth.qty = 1;
    modalCloth.notes = '';
    modalCloth.serviceType = getQueryParam('serviceType') || 'Dry Cleaning';
    modalCloth.pickupSlot = '';
    modalCloth.deliverySlot = '';

    document.getElementById('modalClothImg').src = img;
    document.getElementById('modalClothName').textContent = name;
    document.getElementById('modalClothPrice').textContent = price.toFixed(2);
    document.getElementById('modalServiceType').textContent = modalCloth.serviceType;
    document.getElementById('modalNotes').value = '';
    document.getElementById('modalQty').textContent = '1';

    // Load available slots
    loadAvailableSlots();

    document.getElementById('checkoutModalBg').style.display = 'flex';
}

function loadAvailableSlots() {
    fetch('/available-delivery-slots')
        .then(res => res.json())
        .then(data => {
            const pickupSelect = document.getElementById('modalPickupSlot');
            const deliverySelect = document.getElementById('modalDeliverySlot');

            // Clear existing options (except the default one)
            pickupSelect.innerHTML = '<option value="">Select pickup slot</option>';
            deliverySelect.innerHTML = '<option value="">Select delivery slot</option>';

            // Populate with available slots
            data.forEach(slot => {
                const optionPickup = document.createElement('option');
                optionPickup.value = `${slot.date}|${slot.startTime}-${slot.endTime}`;
                optionPickup.textContent = `${slot.date} | ${slot.startTime} - ${slot.endTime}`;
                pickupSelect.appendChild(optionPickup);

                const optionDelivery = document.createElement('option');
                optionDelivery.value = `${slot.date}|${slot.startTime}-${slot.endTime}`;
                optionDelivery.textContent = `${slot.date} | ${slot.startTime} - ${slot.endTime}`;
                deliverySelect.appendChild(optionDelivery);
            });
        })
        .catch(err => {
            console.error('Error loading slots:', err);
        });
}

function closeCheckoutModal() {
    document.getElementById('checkoutModalBg').style.display = 'none';
}

function changeModalQty(delta) {
    modalCloth.qty = Math.max(1, modalCloth.qty + delta);
    document.getElementById('modalQty').textContent = modalCloth.qty;
}

function toggleCartModal() {
    document.getElementById('cartModalBg').style.display = 'flex';
    renderCartModalItems();
}

function closeCartModal() {
    document.getElementById('cartModalBg').style.display = 'none';
}

document.addEventListener('DOMContentLoaded', function() {
    // Set service type title
    const serviceType = getQueryParam('serviceType') || 'Dry Cleaning';
    document.getElementById('serviceTypeTitle').textContent = serviceType;
    document.getElementById('modalServiceType').textContent = serviceType;
    updateCartCount();

    document.querySelector('.wash-bag-btn').addEventListener('click', function(e) {
        e.stopPropagation();
        toggleCartModal();
    });

    // Handle slot selection
    document.addEventListener('change', function(e) {
        if (e.target.id === 'modalPickupSlot') {
            modalCloth.pickupSlot = e.target.value;
        }
        if (e.target.id === 'modalDeliverySlot') {
            modalCloth.deliverySlot = e.target.value;
        }
    });
});

function addModalToCart() {
    modalCloth.notes = document.getElementById('modalNotes').value;
    modalCloth.pickupSlot = document.getElementById('modalPickupSlot').value;
    modalCloth.deliverySlot = document.getElementById('modalDeliverySlot').value;

    // Validate that slots are selected
    if (!modalCloth.pickupSlot || !modalCloth.deliverySlot) {
        alert('Please select both pickup and delivery time slots');
        return;
    }

    let cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    cart.push({
        clothName: modalCloth.name,
        imageUrl: modalCloth.img,
        price: modalCloth.price,
        quantity: modalCloth.qty,
        notes: modalCloth.notes,
        serviceType: modalCloth.serviceType,
        pickupSlot: modalCloth.pickupSlot,
        deliverySlot: modalCloth.deliverySlot,
        specialInstructions: modalCloth.notes // Use notes as special instructions
    });
    localStorage.setItem('laundryCart', JSON.stringify(cart));
    closeCheckoutModal();
    showCartNotification('Added to basket!');
    updateCartCount();
}

function showCartNotification(message) {
    const toast = document.createElement('div');
    toast.style.cssText = `position:fixed;top:20px;right:20px;background:#4CAF50;color:white;padding:15px 20px;border-radius:8px;z-index:1001;animation:slideInRight 0.3s ease;`;
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => { toast.remove(); }, 2000);
}

function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    const cartCount = document.getElementById('cartCount');
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    cartCount.textContent = totalItems;
    cartCount.style.display = totalItems > 0 ? 'flex' : 'none';
}

function renderCartModalItems() {
    const cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    const cartModalItems = document.getElementById('cartModalItems');
    if (cart.length === 0) {
        cartModalItems.innerHTML = `<div class='cart-empty'><i class='fas fa-shopping-basket' style='font-size:48px;color:#ddd;margin-bottom:15px;'></i><p>Your washing basket is empty</p></div>`;
        document.getElementById('cartModalSubtotal').textContent = 'Rs 0.00';
    } else {
        cartModalItems.innerHTML = cart.map((item, index) => `
            <div class='cart-item'>
                <img src='${item.imageUrl}' alt='${item.clothName}' class='cart-item-image'>
                <div class='cart-item-details'>
                    <div class='cart-item-name'>${item.clothName}</div>
                    <div class='cart-item-service'>${item.serviceType}</div>
                </div>
                <div class='cart-item-price'>Rs ${item.price.toFixed(2)}</div>
                <div class='cart-item-quantity'>
                    <button class='quantity-btn' onclick='updateCartQuantity(${index}, -1)'>−</button>
                    <span class='quantity-display'>${item.quantity}</span>
                    <button class='quantity-btn' onclick='updateCartQuantity(${index}, 1)'>+</button>
                </div>
                <button class='cart-item-remove' onclick='removeCartItem(${index})'>×</button>
            </div>
        `).join('');
        const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
        document.getElementById('cartModalSubtotal').textContent = 'Rs ' + total.toFixed(2);
    }
}

function updateCartQuantity(index, change) {
    let cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    if (cart[index]) {
        cart[index].quantity += change;
        if (cart[index].quantity <= 0) {
            removeCartItem(index);
        } else {
            localStorage.setItem('laundryCart', JSON.stringify(cart));
            renderCartModalItems();
            updateCartCount();
        }
    }
}

function removeCartItem(index) {
    let cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    cart.splice(index, 1);
    localStorage.setItem('laundryCart', JSON.stringify(cart));
    renderCartModalItems();
    updateCartCount();
    showCartNotification('Item removed from basket!');
}

function checkoutCart() {
    const cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    if (cart.length === 0) {
        alert('Your cart is empty!');
        return;
    }
    // Redirect to payment page
    window.location.href = '/payment';
}
