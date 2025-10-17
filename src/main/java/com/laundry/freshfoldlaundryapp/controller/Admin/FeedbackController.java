package com.laundry.freshfoldlaundryapp.controller.Admin;



import com.laundry.freshfoldlaundryapp.dto.Admin.FeedbackManagementDTO;
import com.laundry.freshfoldlaundryapp.service.admin.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String showFeedbackPage(Model model) {
        model.addAttribute("feedbackList", feedbackService.getAllFeedback());
        model.addAttribute("stats", feedbackService.getFeedbackStatistics());
        return "admin/FeedbackManagement";
    }

    @PostMapping("/update")
    public String updateFeedback(@RequestParam int feedbackId,
                                 @RequestParam String response,
                                 @RequestParam String status) {
        feedbackService.updateFeedback(feedbackId, response, status);
        return "redirect:/feedback";
    }

    @GetMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable int id) {
        feedbackService.deleteFeedback(id);
        return "redirect:/feedback";
    }
}
