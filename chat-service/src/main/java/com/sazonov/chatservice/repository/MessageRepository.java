package com.sazonov.chatservice.repository;

import com.sazonov.chatservice.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.chat.id = ?1")
    List<Message> findByChatId(Long chatId);
}
