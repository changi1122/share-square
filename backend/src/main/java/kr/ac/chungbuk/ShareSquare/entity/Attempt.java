package kr.ac.chungbuk.ShareSquare.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "attempt")
    private User user;
    private int attempts;

    public Attempt() {
        attempts = 0;
    }
}
