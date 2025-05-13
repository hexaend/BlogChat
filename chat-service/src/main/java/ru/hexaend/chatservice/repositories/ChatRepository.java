package ru.hexaend.chatservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hexaend.chatservice.models.Message;
import ru.hexaend.chatservice.models.PrivateChat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<PrivateChat, Long> {
    Optional<PrivateChat> findByAuthorIdAndRecipientId(String authorId, String recipientId);

    Optional<PrivateChat> findByRecipientId(String recipientId);

    Page<PrivateChat> findAllByAuthorId(String authorId, Pageable pageable);

    Optional<PrivateChat> findFirstById(Long id);
}
