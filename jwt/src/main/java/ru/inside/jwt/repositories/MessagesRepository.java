package ru.inside.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inside.jwt.models.Message;

public interface MessagesRepository extends JpaRepository<Message, Long> {
}
