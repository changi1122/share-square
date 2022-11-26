package kr.ac.chungbuk.ShareSquare.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;
    private Boolean is_deleted;
    private Boolean is_child;
    private Long parent_id;
    private Long community_id;
    private Long user_id;
}
