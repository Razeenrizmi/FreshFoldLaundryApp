package com.laundry.freshfoldlaundryapp.service.admin;

import com.laundry.freshfoldlaundryapp.dto.Admin.FeedbackManagementDTO;
import com.laundry.freshfoldlaundryapp.dto.Admin.FeedbackManagementDTO;
import com.laundry.freshfoldlaundryapp.DAO.FeedbackDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {
    private final FeedbackDAO feedbackDAO;

    public FeedbackService() {
        this.feedbackDAO = new FeedbackDAO();
    }

    public List<FeedbackManagementDTO> getAllFeedback() {
        return feedbackDAO.getAllFeedback();
    }

    public boolean updateFeedback(int feedbackId, String response, String status) {
        return feedbackDAO.updateFeedback(feedbackId, response, status);
    }

    public boolean deleteFeedback(int feedbackId) {
        return feedbackDAO.deleteFeedback(feedbackId);
    }

    public Map<String, Integer> getFeedbackStatistics() {
        List<FeedbackManagementDTO> allFeedback = feedbackDAO.getAllFeedback();
        Map<String, Integer> stats = new HashMap<>();

        stats.put("total", allFeedback.size());
        stats.put("pending", (int) allFeedback.stream().filter(f -> "Pending".equals(f.getStatus())).count());
        stats.put("inProgress", (int) allFeedback.stream().filter(f -> "In Progress".equals(f.getStatus())).count());
        stats.put("resolved", (int) allFeedback.stream().filter(f -> "Resolved".equals(f.getStatus())).count());

        return stats;
    }
}