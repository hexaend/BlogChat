package ru.hexaend.chatservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hexaend.chatservice.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
