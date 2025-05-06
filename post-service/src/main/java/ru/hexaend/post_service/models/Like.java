package ru.hexaend.post_service.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "likes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"post"})
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @CreatedBy
    private String author;

    @Column(nullable = false)
    private String username;

    @JoinColumn(nullable = false, name = "post_id")
    @ManyToOne
    private Post post;

    // TODO: СДЕЛАТЬ ФУНКЦИОНАЛЬНОСТЬ
//    @Transient
//    private boolean liked = false;
}
