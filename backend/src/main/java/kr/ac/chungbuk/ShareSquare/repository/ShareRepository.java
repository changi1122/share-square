package kr.ac.chungbuk.ShareSquare.repository;

import kr.ac.chungbuk.ShareSquare.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareRepository extends JpaRepository<Share,Long>, JpaSpecificationExecutor<Share> {

    @Query(nativeQuery = true, value = "SELECT * FROM Share s WHERE s.is_deleted = false;")
    List<Share> findByIs_deleted ();

    @Query(nativeQuery = true, value = "SELECT * from Share s WHERE s.user_id = :id and s.is_deleted =false ORDER BY s.created_at DESC ;")
    List<Share> SelectByUserId(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM Share s WHERE s.is_deleted = false ORDER BY s.created_at DESC LIMIT 4 ")
    List<Share> SelectRecentP();

}
