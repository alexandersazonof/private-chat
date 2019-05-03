package com.sazonov.chatservice.form;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatForm {

    private String name;
    private List<Integer> members;


}
