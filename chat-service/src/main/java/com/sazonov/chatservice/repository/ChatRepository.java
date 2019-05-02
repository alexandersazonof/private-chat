package com.sazonov.chatservice.repository;

import com.sazonov.chatservice.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

   @Query("select c from Chat c join Members m on m.chat.id = c.id where m.user.id = ?1")
    List<Chat> findByUserId(Long userId);
}
