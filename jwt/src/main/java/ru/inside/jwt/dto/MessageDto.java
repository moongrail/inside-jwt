package ru.inside.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.inside.jwt.models.Message;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    @NotEmpty
    @NotNull
    private String name;


    @NotNull
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
