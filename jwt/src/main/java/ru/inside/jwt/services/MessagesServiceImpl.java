package ru.inside.jwt.services;

import org.springframework.stereotype.Service;
import ru.inside.jwt.dto.MessageDto;
import ru.inside.jwt.models.Message;

@Service
public class MessagesServiceImpl implements MessagesService {

    @Override
    public void save(MessageDto message) {

        Message newMessage = Message.builder()
                .message(message.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
