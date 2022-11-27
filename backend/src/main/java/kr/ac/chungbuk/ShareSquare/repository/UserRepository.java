package kr.ac.chungbuk.ShareSquare.repository;

import kr.ac.chungbuk.ShareSquare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM Users u WHERE u.id = :id ;")
    User SendUserInfoC(@Param("id")Long id);
}
