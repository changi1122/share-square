package kr.ac.chungbuk.ShareSquare.dtos;

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
public class CommunityDto implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String created_at;
    private String  deleted_at;
    private Boolean is_deleted;
    private Integer visiter;
    private String filename;
    private String filepath;
    private Long user_id;
}