package com.laundry.freshfoldlaundryapp.controller.Admin;


import com.laundry.freshfoldlaundryapp.dto.Admin.PaymentManagementDTO;
import com.laundry.freshfoldlaundryapp.service.admin.PaymentManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PaymentManagementController {
    private PaymentManagementService paymentService = new PaymentManagementService();

    @GetMapping("/payment-management")
    public String paymentManagement(Model model) {
        List<PaymentManagementDTO> paymentList = paymentService.getAllPayments();
        model.addAttribute("paymentList", paymentList);
        return "admin/payment-management";
    }

    @GetMapping("/payment-management/filter")
    public String filterPayments(
            @RequestParam(required = false) String status,
            Model model) {

        List<PaymentManagementDTO> paymentList = paymentService.filterPaymentsByStatus(status);
        model.addAttribute("paymentList", paymentList);
        model.addAttribute("selectedStatus", status);
        return "admin/payment-management";
    }
}
