// Payment page JavaScript
document.addEventListener('DOMContentLoaded', function() {
    loadOrderSummary();
    setupPaymentMethodToggle();
    setupCardFormatting();
    updateCardBrand();
});

function loadOrderSummary() {
    const cart = JSON.parse(localStorage.getItem('laundryCart')) || [];
    const orderItems = document.getElementById('orderItems');

    if (cart.length === 0) {
        orderItems.innerHTML = '<div class="order-item"><span>Wash & Fold Service</span><span>Rs 500.00</span></div>';
        // Set default values if no cart
        document.getElementById('subtotalAmount').textContent = 'Rs 500.00';
        document.getElementById('taxAmount').textContent = 'Rs 75.00';
        document.getElementById('deliveryAmount').textContent = 'Rs 200.00';
        document.getElementById('totalAmount').textContent = 'Rs 775.00';
        return;
    }

    orderItems.innerHTML = cart.map(item => `
        <div class="order-item">
            <img src="${item.imageUrl}" alt="${item.clothName}" class="order-item-image">
            <div class="order-item-details">
                <div class="order-item-name">${item.clothName}</div>
                <div class="order-item-desc">${item.serviceType}, Qty: ${item.quantity}</div>
            </div>
            <div class="order-item-price">Rs ${(item.price * item.quantity).toFixed(2)}</div>
        </div>
    `).join('');

    calculateTotals(cart);
}

function calculateTotals(cart) {
    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const deliveryFee = 200.00;
    const taxRate = 0.15;
    const tax = subtotal * taxRate;
    const total = subtotal + tax + deliveryFee;

    document.getElementById('subtotalAmount').textContent = `Rs ${subtotal.toFixed(2)}`;
    document.getElementById('taxAmount').textContent = `Rs ${tax.toFixed(2)}`;
    document.getElementById('deliveryAmount').textContent = `Rs ${deliveryFee.toFixed(2)}`;
    document.getElementById('totalAmount').textContent = `Rs ${total.toFixed(2)}`;
}

function setupPaymentMethodToggle() {
    const cardPayment = document.getElementById('cardPayment');
    const cashOnDelivery = document.getElementById('cashOnDelivery');
    const cardForm = document.getElementById('cardForm');
    const codForm = document.getElementById('codForm');
    const checkoutBtnText = document.getElementById('checkoutBtnText');

    if (!cardPayment || !cashOnDelivery) return;

    cardPayment.addEventListener('change', function() {
        if (this.checked) {
            cardForm.style.display = 'block';
            codForm.style.display = 'none';
            if (checkoutBtnText) checkoutBtnText.textContent = 'Process Payment';
        }
    });

    cashOnDelivery.addEventListener('change', function() {
        if (this.checked) {
            cardForm.style.display = 'none';
            codForm.style.display = 'block';
            if (checkoutBtnText) checkoutBtnText.textContent = 'Place Order';
        }
    });
}

function setupCardFormatting() {
    const cardNumber = document.getElementById('cardNumber');
    const expires = document.getElementById('expires');
    const cvc = document.getElementById('cvc');

    if (cardNumber) {
        // Format card number with spaces
        cardNumber.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s/g, '').replace(/[^0-9]/gi, '');
            let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
            if (formattedValue.length > 19) formattedValue = formattedValue.substring(0, 19);
            e.target.value = formattedValue;
            updateCardBrand();
        });
    }

    if (expires) {
        // Format expiry date
        expires.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            e.target.value = value;
        });
    }

    if (cvc) {
        // Format CVC
        cvc.addEventListener('input', function(e) {
            e.target.value = e.target.value.replace(/[^0-9]/g, '');
        });
    }
}

function updateCardBrand() {
    const cardType = document.getElementById('cardType');
    const cardBrand = document.getElementById('cardBrand');
    const cardNumber = document.getElementById('cardNumber');

    if (!cardType || !cardBrand) return;

    // Update brand based on dropdown selection
    cardType.addEventListener('change', function() {
        cardBrand.textContent = this.value.toUpperCase();
    });

    // Auto-detect card type based on number
    if (cardNumber && cardNumber.value) {
        const firstDigit = cardNumber.value.replace(/\s/g, '')[0];
        if (firstDigit === '4') {
            cardType.value = 'visa';
            cardBrand.textContent = 'VISA';
        } else if (firstDigit === '5') {
            cardType.value = 'mastercard';
            cardBrand.textContent = 'MASTERCARD';
        } else if (firstDigit === '3') {
            cardType.value = 'amex';
            cardBrand.textContent = 'AMEX';
        }
    }
}

function processPayment() {
    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked')?.value;

    if (!paymentMethod) {
        alert('Please select a payment method');
        return;
    }

    if (paymentMethod === 'card') {
        if (validateCardForm()) {
            showPaymentSuccess('Card Payment');
        }
    } else {
        if (validateCODForm()) {
            showPaymentSuccess('Cash on Delivery');
        }
    }
}

function validateCardForm() {
    const cardNumber = document.getElementById('cardNumber')?.value || '';
    const cardHolder = document.getElementById('cardHolder')?.value || '';
    const expires = document.getElementById('expires')?.value || '';
    const cvc = document.getElementById('cvc')?.value || '';

    if (!cardNumber || cardNumber.replace(/\s/g, '').length < 13) {
        alert('Please enter a valid card number');
        return false;
    }

    if (!cardHolder.trim()) {
        alert('Please enter card holder name');
        return false;
    }

    if (!expires || expires.length < 5) {
        alert('Please enter expiry date (MM/YY)');
        return false;
    }

    if (!cvc || cvc.length < 3) {
        alert('Please enter CVC');
        return false;
    }

    return true;
}

function validateCODForm() {
    const codName = document.getElementById('codName')?.value || '';
    const codPhone = document.getElementById('codPhone')?.value || '';
    const codAddress = document.getElementById('codAddress')?.value || '';

    if (!codName.trim()) {
        alert('Please enter your full name');
        return false;
    }

    if (!codPhone.trim()) {
        alert('Please enter your phone number');
        return false;
    }

    if (!codAddress.trim()) {
        alert('Please enter your delivery address');
        return false;
    }

    return true;
}

function showPaymentSuccess(method) {
    const cart = JSON.parse(localStorage.getItem('laundryCart')) || [
        {
            clothName: 'Mixed Items',
            serviceType: 'Wash & Fold',
            price: 500,
            quantity: 1,
            notes: 'Default order',
            imageUrl: '/images/WashnFold.jpg'
        }
    ];

    const total = document.getElementById('totalAmount')?.textContent || 'Rs 775.00';

    // Prepare payment data for backend
    const paymentData = {
        cartItems: cart,
        paymentMethod: method.toLowerCase().includes('card') ? 'card' : 'cod',
        subtotal: parseFloat(document.getElementById('subtotalAmount')?.textContent?.replace('Rs ', '') || '500'),
        tax: parseFloat(document.getElementById('taxAmount')?.textContent?.replace('Rs ', '') || '75'),
        delivery: parseFloat(document.getElementById('deliveryAmount')?.textContent?.replace('Rs ', '') || '200'),
        total: parseFloat(total.replace('Rs ', ''))
    };

    // Add payment method specific details
    if (paymentData.paymentMethod === 'card') {
        paymentData.cardDetails = {
            cardNumber: document.getElementById('cardNumber')?.value || '',
            cardHolder: document.getElementById('cardHolder')?.value || '',
            expires: document.getElementById('expires')?.value || '',
            cvc: document.getElementById('cvc')?.value || '',
            cardType: document.getElementById('cardType')?.value || 'visa'
        };
    } else {
        paymentData.codDetails = {
            fullName: document.getElementById('codName')?.value || '',
            phoneNumber: document.getElementById('codPhone')?.value || '',
            address: document.getElementById('codAddress')?.value || ''
        };
    }

    // Show loading message
    const originalButton = document.querySelector('.checkout-btn');
    if (originalButton) {
        originalButton.disabled = true;
        originalButton.textContent = 'Processing...';
    }

    // Get CSRF token from meta tags
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    // Send payment data to backend
    const headers = {
        'Content-Type': 'application/json',
    };

    // Add CSRF token if available
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    fetch('/payment/process', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(paymentData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Clear cart after successful payment
            localStorage.removeItem('laundryCart');

            // Show success message based on payment method
            const message = paymentData.paymentMethod === 'card' ?
                `Payment submitted for verification!\nMethod: ${method}\nTotal: ${total}\n\nYour order will be processed after payment verification.` :
                `Order placed successfully!\nMethod: ${method}\nTotal: ${total}\n\nYou will pay on delivery.`;

            alert(message);

            // Redirect to customer dashboard
            window.location.href = '/customer-dashboard';
        } else {
            alert('Payment failed: ' + (data.message || 'Unknown error'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Payment processing failed. Please try again.');
    })
    .finally(() => {
        // Restore button
        if (originalButton) {
            originalButton.disabled = false;
            originalButton.textContent = 'Place Order';
        }
    });
}

function goBack() {
    window.history.back();
}

// Toggle between card and COD forms (for simple HTML usage)
function togglePaymentForms() {
    const cardForm = document.getElementById('cardForm');
    const codForm = document.getElementById('codForm');
    const cardRadio = document.getElementById('cardPayment');

    if (cardForm && codForm && cardRadio) {
        if (cardRadio.checked) {
            cardForm.style.display = 'block';
            codForm.style.display = 'none';
        } else {
            cardForm.style.display = 'none';
            codForm.style.display = 'block';
        }
    }
}
