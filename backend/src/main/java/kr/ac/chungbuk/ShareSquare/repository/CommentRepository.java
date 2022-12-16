package kr.ac.chungbuk.ShareSquare.repository;

import kr.ac.chungbuk.ShareSquare.Interface.CommentInterface;
import kr.ac.chungbuk.ShareSquare.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(nativeQuery = true, value ="SELECT c.id, c.content, c.created_at, c.is_child, c.parent_id, c.community_id, u.username, u.profile_image FROM comment c INNER JOIN users u on c.user_id = u.id WHERE c.is_deleted = false AND c.community_id = :id ORDER BY c.parent_id asc , c.created_at asc;" )
    List<CommentInterface> searchAllByCommunity_idAndIs_deletedFalse(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT DISTINCT c.community_id from comment c WHERE c.is_deleted = false AND :uid = c.user_Id ORDER BY c.created_at DESC ;")
    List<Long> searchCommentByUser_id(@Param("uid") Long uid);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM comment c where c.is_deleted = false AND c.community_id = :id ;")
    Integer Number_of_ROW(@Param("id")Long id);

    @Query(nativeQuery = true, value = "SELECT c.parent_id from comment c WHERE c.id != :id AND c.parent_id LIKE :id AND c.is_deleted = false;")
    List<Long> IsExistAtParent_Id(@Param("id")Long id);

}
