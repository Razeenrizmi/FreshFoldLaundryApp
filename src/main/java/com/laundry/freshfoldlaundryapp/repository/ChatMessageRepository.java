package com.laundry.freshfoldlaundryapp.repository;

import com.laundry.freshfoldlaundryapp.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderIdAndReceiverIdOrderByTimestampAsc(Long senderId, Long receiverId);
    List<ChatMessage> findByReceiverIdAndIsReadFalse(Long receiverId);
    // Fetch all messages between two users (both directions)
    List<ChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(Long senderId1, Long receiverId1, Long senderId2, Long receiverId2);
}
