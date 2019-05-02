package com.sazonov.chatservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "members")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @JoinColumn(name = "user_id")
    @OneToOne
    private User user;

    @JoinColumn(name = "chat_id")
    @OneToOne
    private Chat chat;
}
