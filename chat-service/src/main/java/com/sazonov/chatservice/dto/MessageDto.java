package com.sazonov.chatservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class MessageDto {

    @NotBlank
    private String value;

    @NotBlank
    private Date date;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long chatId;
}
