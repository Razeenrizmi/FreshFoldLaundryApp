package com.laundry.freshfoldlaundryapp.service.admin;

import com.laundry.freshfoldlaundryapp.dto.Admin.PaymentManagementDTO;
import com.laundry.freshfoldlaundryapp.DAO.PaymentManagementDAO;

import java.util.List;

public class PaymentManagementService {
    private PaymentManagementDAO paymentDAO = new PaymentManagementDAO();

    public List<PaymentManagementDTO> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    public List<PaymentManagementDTO> filterPaymentsByStatus(String status) {
        return paymentDAO.filterPaymentsByStatus(status);
    }
}