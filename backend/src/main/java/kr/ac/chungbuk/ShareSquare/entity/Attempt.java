package kr.ac.chungbuk.ShareSquare.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Attempt {

    private @Id
    @GeneratedValue Long id;
    private Long userId;
    private int attempts;

    protected Attempt() { }

    public Attempt(Long userId) {
        this.userId = userId;
        attempts = 0;
    }
}
