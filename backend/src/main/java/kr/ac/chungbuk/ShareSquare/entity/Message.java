package kr.ac.chungbuk.ShareSquare.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String message_text;

    private Long message_from;

    private Long message_to;

    private LocalDateTime created_datetime;

}
