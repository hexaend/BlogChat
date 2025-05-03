package ru.hexaend.post_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hexaend.post_service.models.Like;
import ru.hexaend.post_service.models.Post;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
  Optional<Like> findByPostAndAuthor(Post post, String author);
}