package kr.ac.chungbuk.ShareSquare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community")
@ToString(exclude = "user")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;
    private Boolean is_deleted;
    private Integer visiter;
    private String filename;
    private String filepath;
    private Long user_id;
}
