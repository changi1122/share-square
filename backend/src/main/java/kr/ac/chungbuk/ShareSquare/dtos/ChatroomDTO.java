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
public class ChatroomDTO implements Serializable {
    private Long id;
    private String last_act;
    private String guest_name;
    private Long user_id;
    private Long guest_id;
    private Boolean is_deleted;
}