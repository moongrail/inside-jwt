package ru.inside.jwt.services;

import ru.inside.jwt.dto.MessageDto;

public interface MessagesService {

    void save(MessageDto message,String name);
}
