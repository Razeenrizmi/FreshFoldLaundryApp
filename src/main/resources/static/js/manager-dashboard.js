// Manager Dashboard JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Initialize dashboard
    updateStats();
});

// Payment verification function
function verifyPayment(orderId, action) {
    // Get CSRF token from meta tags
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    const formData = new FormData();
    formData.append('orderId', orderId);
    formData.append('action', action);

    const headers = {};
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    fetch('/manager/verify-payment', {
        method: 'POST',
        headers: headers,
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showNotification(data.message, 'success');
            // Remove the row from the table
            const row = document.querySelector(`tr[data-order-id="${orderId}"]`);
            if (row) {
                row.remove();
            }
            // Reload page to update stats
            setTimeout(() => {
                location.reload();
            }, 1500);
        } else {
            showNotification(data.message, 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showNotification('Error processing payment verification', 'error');
    });
}

// Staff assignment function
function assignStaff(orderId) {
    const staffSelect = document.getElementById(`staff-${orderId}`);
    const staffId = staffSelect.value;

    if (!staffId) {
        showNotification('Please select a staff member', 'error');
        return;
    }

    // Get CSRF token from meta tags
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    const formData = new FormData();
    formData.append('orderId', orderId);
    formData.append('staffId', staffId);

    const headers = {};
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    fetch('/manager/assign-staff', {
        method: 'POST',
        headers: headers,
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showNotification(data.message, 'success');
            // Remove the row from the table
            const row = document.querySelector(`tr[data-order-id="${orderId}"]`);
            if (row) {
                row.remove();
            }
            // Reload page to update stats
            setTimeout(() => {
                location.reload();
            }, 1500);
        } else {
            showNotification(data.message, 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showNotification('Error assigning staff', 'error');
    });
}

// Delivery assignment function
function assignDelivery(orderId) {
    const deliverySelect = document.getElementById(`delivery-${orderId}`);
    const deliveryStaffId = deliverySelect.value;

    if (!deliveryStaffId) {
        showNotification('Please select a delivery staff member', 'error');
        return;
    }

    // Get CSRF token from meta tags
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    const formData = new FormData();
    formData.append('orderId', orderId);
    formData.append('deliveryStaffId', deliveryStaffId);

    const headers = {};
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    fetch('/manager/assign-delivery', {
        method: 'POST',
        headers: headers,
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showNotification(data.message, 'success');
            // Remove the row from the table
            const row = document.querySelector(`tr[data-order-id="${orderId}"]`);
            if (row) {
                row.remove();
            }
            // Reload page to update stats
            setTimeout(() => {
                location.reload();
            }, 1500);
        } else {
            showNotification(data.message, 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showNotification('Error assigning delivery', 'error');
    });
}

// Show notification function
function showNotification(message, type = 'success') {
    const notification = document.getElementById('notification');
    const notificationMessage = notification.querySelector('.notification-message');
    const notificationIcon = notification.querySelector('.notification-icon');

    // Set message
    notificationMessage.textContent = message;

    // Set icon and style based on type
    notification.className = `notification ${type}`;
    if (type === 'success') {
        notificationIcon.className = 'notification-icon fas fa-check-circle';
    } else {
        notificationIcon.className = 'notification-icon fas fa-exclamation-circle';
    }

    // Show notification
    notification.classList.add('show');

    // Hide after 3 seconds
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

// Update dashboard stats
function updateStats() {
    // Add any additional stat updates here if needed
    console.log('Dashboard stats updated');
}

// Auto-refresh dashboard every 30 seconds
setInterval(() => {
    // You can add auto-refresh logic here if needed
    // location.reload();
}, 30000);
