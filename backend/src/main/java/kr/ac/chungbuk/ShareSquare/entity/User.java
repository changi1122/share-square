package kr.ac.chungbuk.ShareSquare.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;
    private String password;
    private String role;
    private String email;

    private boolean isLocked;
    private boolean isBanned;
    private boolean isDeleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime deletedAt;
    private int reliability;
    private String profileImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id")
    private Attempt attempt;

    protected User() { }

    public User(String username, String password, String email, String role, String profileImage) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.profileImage = profileImage;

        isLocked = false;
        isBanned = false;
        isDeleted = false;
        createdAt = deletedAt = OffsetDateTime.now();
        reliability = 0;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(this.role));

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return (!isLocked && !isBanned && !isDeleted);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return (!isLocked && !isBanned && !isDeleted);
    }
}
