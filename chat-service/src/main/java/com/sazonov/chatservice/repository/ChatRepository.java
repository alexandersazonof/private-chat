package com.sazonov.chatservice.repository;

import com.sazonov.chatservice.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {


    @Query("select c from Chat c join c.users u where u.id = ?1")
    List<Chat> findByUserId(Long userId);

    @Query("select c from Chat  c join c.users u where u.id = :userId and upper(c.name) like CONCAT('%',UPPER(:value),'%') ")
    List<Chat> findByUserId(Long userId, String value);

}
