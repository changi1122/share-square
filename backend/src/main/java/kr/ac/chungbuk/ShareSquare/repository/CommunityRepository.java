package kr.ac.chungbuk.ShareSquare.repository;

import kr.ac.chungbuk.ShareSquare.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {

    @Query(nativeQuery = true, value= "SELECT * FROM Community m WHERE m.id > :id and m.is_deleted = false ORDER BY m.id ASC LIMIT 1")
    List<Community> findNext(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM Community m WHERE m.id < :id and m.is_deleted = false ORDER BY m.id DESC LIMIT 1; ")
    List<Community> findPrevios(@Param("id") Long id);

    @Query(nativeQuery = true, value= "SELECT * FROM Community m WHERE m.id > :id and m.is_deleted = false ORDER BY m.id ASC LIMIT 1")
    Community Next(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM Community m WHERE m.id < :id and m.is_deleted = false ORDER BY m.id DESC LIMIT 1; ")
    Community Previos(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT * from Community m WHERE m.user_id = :id and m.is_deleted =false ORDER BY m.created_at DESC ;")
    List<Community> SelectByUserId(@Param("id") Long id);
}