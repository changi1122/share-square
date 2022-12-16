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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    /**
     * @param username
     * @return username가 받은 값인 user를 찾아 User(UserDetails의 자식 클래스) 객체를 반환합니다.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        return user;
    }

    /**
     * @param id
     * @return id가 받은 값인 user를 찾아 User(UserDetails의 자식 클래스) 객체를 반환합니다.
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        return user;
    }


    /**
     * user를 생성합니다.
     * @param username
     * @param password
     * @param email
     */
    public void create(String username, String password, String email, String profileImage, MultipartFile image)
            throws Exception {
        if (!userRepository.existsByUsername(username) && !username.equals("anonymousUser")) {
            String savePath = System.getProperty("user.dir") +
                    "/src/main/resources/static/resource/profile";
            if (!new File(savePath).exists())
                new File(savePath).mkdir();

            if (profileImage.equals("upload")) {
                try {
                    String filePath = savePath + "/" + username + ".jpg";
                    image.transferTo(new File(filePath));
                } catch (Exception e) {
                    profileImage = "man1";
                }
            }

            if (!profileImage.equals("upload")) {
                String originPath = System.getProperty("user.dir") +
                        String.format("/src/main/resources/static/resource/profile/default/%s.jpg", profileImage);
                File origin = new File(originPath);
                String destPath = System.getProperty("user.dir") +
                        String.format("/src/main/resources/static/resource/profile/%s.jpg", username);
                File dest = new File(destPath);
                FileCopyUtils.copy(origin, dest);
            }

            User user = new User(
                    username,
                    passwordEncoder.encode(password),
                    email,
                    "USER",
                    profileImage
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

    /**
     * 주어진 username인 user를 찾아 password가 일치하면 newPassword(선택적)와 email을 수정합니다.
     * @param username 필수 (변경 불가)
     * @param password 기존 비밀번호 필수
     * @param newPassword 새 비밀번호(선택적)
     * @param email 새 이메일(선택적)
     * @throws Exception
     */
    public void update(String username, String password, String newPassword, String email) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            if (newPassword != null && !newPassword.isBlank()) {
                user.setPassword(passwordEncoder.encode(newPassword));
            }
            if (email != null && !email.isBlank()) {
                user.setEmail(email);
            }
            userRepository.save(user);
        }
        else {
            throw new IllegalArgumentException("Password not matched");
        }
    }

    /**
     * 주어진 username인 user의 프로필 이미지를 수정합니다.
     * @param username 필수
     * @param profileImage 'upload' or default 이미지 이름
     * @param image 이미지 파일
     * @throws Exception
     */
    public void changeProfileImage(String username, String profileImage, MultipartFile image) throws Exception {
        String directoryPath = System.getProperty("user.dir") +
                "/src/main/resources/static/resource/profile";
        if (!new File(directoryPath).exists())
            new File(directoryPath).mkdir();

        if (profileImage.equals("upload") && image != null) {
            try {
                String filePath = System.getProperty("user.dir") +
                        "/src/main/resources/static/resource/profile/" + username + ".jpg";
                if (new File(filePath).exists())
                    new File((filePath)).delete();

                image.transferTo(new File(filePath));
            } catch (Exception e) {
                profileImage = "man1";
            }
        }

        if (!profileImage.equals("upload")) {
            String filePath = System.getProperty("user.dir") +
                    "/src/main/resources/static/resource/profile/" + username + ".jpg";
            if (new File(filePath).exists())
                new File((filePath)).delete();

            String originPath = System.getProperty("user.dir") +
                    String.format("/src/main/resources/static/resource/profile/default/%s.jpg", profileImage);
            File origin = new File(originPath);
            String destPath = System.getProperty("user.dir") +
                    String.format("/src/main/resources/static/resource/profile/%s.jpg", username);
            File dest = new File(destPath);
            FileCopyUtils.copy(origin, dest);
        }
    }

    /**
     * 주어진 username인 user를 삭제합니다.
     * @param username
     * @throws Exception
     */
    public void delete(String username) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));

        user.setDeleted(true);
        user.setDeletedAt(OffsetDateTime.now());
        userRepository.save(user);
    }


    /**
     * username과 password로 로그인을 시도합니다. 100회 실패시 로그인이 잠깁니다.
     * 성공시 엑세스 토큰을 반환합니다.
     * @param username
     * @param password
     * @return token 반환
     * @throws UsernameNotFoundException username 존재하지 않는 경우, user가 밴 또는 잠금 또는 삭제된 경우
     */
    public String tryLogin(String username, String password) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));

        if (user.isBanned() || user.isLocked() || user.isDeleted())
            throw new UsernameNotFoundException("User not present");

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

    /**
     * username 중복을 확인하여 사용할 수 있는지 반환합니다.
     * @param username
     * @return 사용할 수 있으면 true, 없으면 false
     */
    public boolean canUseAsUsername(String username) {
        return !userRepository.existsByUsername(username) && !username.equals("anonymousUser");
    }

    /**
     * username의 user 신뢰도를 반환합니다.
     * @param username
     */
    public int getReliability(String username) {
        try {
            User user = (User)userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not present"));
            return user.getReliability();
        }
        catch (Exception e) {
            return 0;
        }
    }

    /**
     * user의 신뢰도를 reliability로 설정합니다.
     * @param user 신뢰도를 설정할 user 객체
     * @param reliability 신뢰도 정수 값
     */
    public void setReliability(User user, int reliability) {
        user.setReliability(reliability);
        userRepository.save(user);
    }

    /**
     * user의 신뢰도에 양수 또는 음수 diff를 더합니다.
     * @param user 신뢰도를 설정할 user 객체
     * @param diff 더할 신뢰도 값 (양수/음수 가능)
     */
    public void changeReliability(User user, int diff) {
        int reliability = user.getReliability() + diff;
        if (reliability < 0) reliability = 0;

        user.setReliability(reliability);
        userRepository.save(user);
    }

    public String findUsernameByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        return user.getUsername();
    }

    /**
     * user를 로그인할 수 없도록 잠급니다.
     * @param user
     */
    public void lock(User user) {
        user.setLocked(true);
        userRepository.save(user);
    }

    /**
     * user를 로그인할 수 없도록 밴 합니다.
     * @param user
     */
    public void ban(User user) {
        user.setBanned(true);
        userRepository.save(user);
    }
}
