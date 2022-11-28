package kr.ac.chungbuk.ShareSquare.repository;

import kr.ac.chungbuk.ShareSquare.entity.Extendinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtendinfoRepository extends JpaRepository<Extendinfo, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM edtendinfo e where e.is_deleted = false;")
    List<Extendinfo> findByIs_deleted();
}
