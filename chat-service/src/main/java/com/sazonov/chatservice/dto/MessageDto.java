package com.sazonov.chatservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MessageDto {

    @NotBlank
    private String value;

    @NotNull
    private Long userId;
}
