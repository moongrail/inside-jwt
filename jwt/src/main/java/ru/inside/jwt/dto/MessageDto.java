package ru.inside.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.inside.jwt.models.Account;
import ru.inside.jwt.models.Message;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private String name;

    private String message;


    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .name(message.getName())
                .message(message.getMessage())
                .build();
    }
    public static List<MessageDto> from(List<Message> messages) {
        return messages.stream().map(MessageDto::from).collect(Collectors.toList());
    }
}
