package com.sazonov.chatservice.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatForm {

    @NotBlank
    private String name;

    @NotBlank
    private List<Long> members;


}
