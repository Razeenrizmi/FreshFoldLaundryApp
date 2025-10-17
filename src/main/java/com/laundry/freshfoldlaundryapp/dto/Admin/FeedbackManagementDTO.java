package com.laundry.freshfoldlaundryapp.dto.Admin;

import java.time.LocalDateTime;

public class FeedbackManagementDTO {
    private int feedbackId;
    private int customerId;
    private String customerName;
    private int orderId;
    private String content;
    private LocalDateTime date;
    private String status;
    private String response;
    private String orderStatus;
    private double orderAmount;

    // constructor


    public FeedbackManagementDTO() {}

    public FeedbackManagementDTO(int feedbackId, int customerId, String customerName, int orderId, String content, LocalDateTime date, String status, String response, String orderStatus, double orderAmount) {
        this.feedbackId = feedbackId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.orderId = orderId;
        this.content = content;
        this.date = date;
        this.status = status;
        this.response = response;
        this.orderStatus = orderStatus;
        this.orderAmount = orderAmount;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }
}
