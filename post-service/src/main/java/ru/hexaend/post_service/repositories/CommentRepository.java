package ru.hexaend.post_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hexaend.post_service.models.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndAuthor(Long commentId, String name);
}