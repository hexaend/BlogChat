package ru.hexaend.post_service.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"post"})
@ToString(exclude = {"post"})
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @CreatedBy
    private String author;

    @Column(nullable = false)
    private String username;

    @JoinColumn(nullable = false, name = "post_id")
    @ManyToOne
    private Post post;
}
