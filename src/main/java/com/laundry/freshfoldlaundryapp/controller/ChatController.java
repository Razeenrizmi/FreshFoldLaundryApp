package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.ChatMessage;
import com.laundry.freshfoldlaundryapp.service.ChatService;
import com.laundry.freshfoldlaundryapp.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ChatMessage sendMessage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @RequestBody Map<String, Object> payload) {
        Long senderId = userDetails.getUserId();
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        String message = payload.get("message").toString();
        return chatService.sendMessage(senderId, receiverId, message);
    }

    @GetMapping("/messages")
    public List<ChatMessage> getMessages(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestParam Long otherUserId) {
        Long userId = userDetails.getUserId();
        return chatService.getMessages(userId, otherUserId);
    }
}

