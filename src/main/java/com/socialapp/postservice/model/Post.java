package com.socialapp.postservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String caption;

    private String image;

    @ColumnDefault("0")
    @Builder.Default
    private Long likeCount = 0L;

    @ColumnDefault("0")
    @Builder.Default
    private Long commentCount = 0L;

    @CreationTimestamp
    private LocalDateTime insertTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;


}
