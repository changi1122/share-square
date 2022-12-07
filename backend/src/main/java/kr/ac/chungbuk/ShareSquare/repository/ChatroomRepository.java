package kr.ac.chungbuk.ShareSquare.repository;

import kr.ac.chungbuk.ShareSquare.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom,Long>, JpaSpecificationExecutor<Chatroom> {

    @Query(nativeQuery = true, value = "SELECT * from chatroom c where c.user_id = :id and c.is_deleted =false ORDER BY c.last_act DESC ;")
    List<Chatroom> findChatroomByIdAndIs_deleted(@Param("id") Long id);
}