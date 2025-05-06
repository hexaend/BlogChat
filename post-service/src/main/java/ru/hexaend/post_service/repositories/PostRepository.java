package ru.hexaend.post_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hexaend.post_service.models.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeletedIsFalseAndAuthor(Long id, String author);

    Optional<Post> findByIdAndDeletedIsFalse(Long id);

    Page<Post> findAllByDeletedIsFalse(Pageable pageable);

    Page<Post> findAllByDeletedIsFalseAndAuthor(String name,
                                                Pageable pageable);
}