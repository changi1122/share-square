package kr.ac.chungbuk.ShareSquare.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "extendinfo")
public class Extendinfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;
    private String content;
    private Boolean is_deleted;
    private Double latitude;
    private Double longtitude;
    private String filename;
    private String filepath;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;
}
