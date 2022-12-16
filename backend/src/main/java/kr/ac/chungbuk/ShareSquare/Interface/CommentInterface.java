package kr.ac.chungbuk.ShareSquare.Interface;

import java.time.LocalDateTime;

public interface CommentInterface {

    Long getId();
    String getContent();
    LocalDateTime getCreated_at();
    Boolean getIs_child();
    Long getParent_id();
    Long getCommunity_id();
    String getUsername();
    String getProfileImage();

}
