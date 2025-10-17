package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.model.ChatMessage;
import com.laundry.freshfoldlaundryapp.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage sendMessage(Long senderId, Long receiverId, String message) {
        ChatMessage chatMessage = new ChatMessage(senderId, receiverId, message, LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessages(Long userId1, Long userId2) {
        return chatMessageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
            userId1, userId2, userId2, userId1
        );
    }

    public List<ChatMessage> getUnreadMessages(Long receiverId) {
        return chatMessageRepository.findByReceiverIdAndIsReadFalse(receiverId);
    }

    public void markMessagesAsRead(List<ChatMessage> messages) {
        for (ChatMessage msg : messages) {
            msg.setRead(true);
        }
        chatMessageRepository.saveAll(messages);
    }
}
