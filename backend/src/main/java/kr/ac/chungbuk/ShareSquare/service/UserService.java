package kr.ac.chungbuk.ShareSquare.service;

import kr.ac.chungbuk.ShareSquare.entity.Attempt;
import kr.ac.chungbuk.ShareSquare.entity.User;
import kr.ac.chungbuk.ShareSquare.repository.AttemptRepository;
import kr.ac.chungbuk.ShareSquare.repository.UserRepository;
import kr.ac.chungbuk.ShareSquare.security.JwtTokenProvider;
import kr.ac.chungbuk.ShareSquare.security.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private AttemptRepository attemptRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       AttemptRepository attemptRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        return user;
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        return user;
    }

    public void create(String username, String password, String email) throws Exception
    {
        if (!userRepository.existsByUsername(username)) {
            User user = new User(
                    username,
                    passwordEncoder.encode(password),
                    email,
                    "USER"
            );
            userRepository.save(user);

            Attempt attempt = new Attempt();
            attemptRepository.save(attempt);

            user.setAttempt(attempt);
            userRepository.save(user);
        }
        else {
            throw new Exception("username is not unique");
        }
    }

    public String tryLogin(String username, String password) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            int failed = user.getAttempt().getAttempts();
            if (failed + 1 < 100) {
                user.getAttempt().setAttempts(failed + 1);
                user.setDeletedAt(OffsetDateTime.now());
                userRepository.save(user);
            }
            else
                this.lock(user);
            throw new IllegalArgumentException("Password not matched");
        }

        if (user.getAttempt().getAttempts() != 0) {
            user.getAttempt().setAttempts(0);
            userRepository.save(user);
        }

        Authentication authentication = new UserAuthentication(username, password, user.getAuthorities());
        String token = JwtTokenProvider.generateToken(authentication);

        return token;
    }

    public void lock(User user) {
        user.setLocked(true);
        userRepository.save(user);
    }

    public void ban(User user) {
        user.setBanned(true);
        userRepository.save(user);
    }
}
