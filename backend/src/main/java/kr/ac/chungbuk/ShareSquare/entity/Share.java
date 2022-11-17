package kr.ac.chungbuk.ShareSquare.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "share")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;
    private Boolean is_deleted;
    private Double latitude;
    private Double longtitude;
    private String filename;
    private String filepath;
    private String category;
    private String username;
    private Long user_id;
}
