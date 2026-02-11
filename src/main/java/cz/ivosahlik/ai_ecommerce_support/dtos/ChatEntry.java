package cz.ivosahlik.ai_ecommerce_support.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatEntry {
    private String role; //user, Assistant, System
    private String content;
}
