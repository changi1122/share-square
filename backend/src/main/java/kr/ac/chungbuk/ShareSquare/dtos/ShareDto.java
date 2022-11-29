package kr.ac.chungbuk.ShareSquare.dtos;

import kr.ac.chungbuk.ShareSquare.entity.Community;
import kr.ac.chungbuk.ShareSquare.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareDto implements Serializable {
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
    private int reliability;

}