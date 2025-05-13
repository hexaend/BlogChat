package ru.hexaend.chatservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hexaend.chatservice.models.Message;
import ru.hexaend.chatservice.models.PrivateChat;

public interface MessageRepository extends JpaRepository<Message, Long> {


    Page<Message> findAllByPrivateChat(PrivateChat privateChat,
                                       Pageable pageable);
}
