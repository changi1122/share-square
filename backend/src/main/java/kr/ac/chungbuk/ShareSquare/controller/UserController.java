package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.entity.User;
import kr.ac.chungbuk.ShareSquare.service.UserService;
import kr.ac.chungbuk.ShareSquare.utility.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("api/get/user")
    public ResponseEntity getUser(){
        User user = (User) userService.loadUserByUsername(Security.getCurrentUsername());

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("profileImage", user.getProfileImage());
        result.put("reliability", user.getReliability());
        result.put("id", user.getId());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(path = "/api/login")
    public ResponseEntity login(final HttpServletRequest req,
                                final HttpServletResponse res,
                                @RequestBody Map<String, String> request) throws Exception {

        try {
            String token = userService.tryLogin(request.get("username"), request.get("password"));
            Cookie tokenCookie = createTokenCookie(token, 168 * 60 * 60);
            res.addCookie(tokenCookie);

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "로그인에 성공하였습니다.");
            return new ResponseEntity(result, HttpStatus.OK);
        }
        catch(Exception e) {
            Cookie tokenCookie = createTokenCookie(null, 0);
            res.addCookie(tokenCookie);

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "아이디 또는 비밀번호가 잘못되었습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/api/logout")
    public ResponseEntity logout(final HttpServletRequest req, final HttpServletResponse res) {
        Cookie tokenCookie = createTokenCookie(null, 0);
        res.addCookie(tokenCookie);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "로그아웃에 성공하였습니다.");
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(path = "/api/currentuser")
    public ResponseEntity getCurrentUserData() {
        HashMap<String, Object> result = new HashMap<>();

        String username = Security.getCurrentUsername();

        result.put("username", username);
        result.put("Authorities", Security.getCurrentUserRole());

        try {
            User currentUser = (User)userService.loadUserByUsername(username);
            result.put("role", currentUser.getRole());
            result.put("email", currentUser.getEmail());
            result.put("reliability", currentUser.getReliability());
            result.put("profileImage", currentUser.getProfileImage());
        } catch (Exception e){
            // 로그인되지 않았거나 오류난 경우
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/api/register")
    public ResponseEntity createUser(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("email") String email,
                                     @RequestParam("profileImage") String profileImage,
                                     @RequestParam(value="image", required = false) MultipartFile image) {
        if (username != null && !username.isBlank() &&
                password != null && !password.isBlank() &&
                email != null && !email.isBlank() &&
                profileImage != null && !profileImage.isBlank()) {

            try {
                userService.create(
                        username,
                        password,
                        email,
                        profileImage,
                        image
                );

                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원가입에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.CREATED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원가입에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "회원가입에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/user/{username}")
    public ResponseEntity updateUser(@PathVariable("username") String username,
                                              @RequestBody Map<String, String> body) {
        if (!username.isBlank() && username.equals(Security.getCurrentUsername()) &&
                body.get("password") != null && !body.get("password").isBlank()) {

            try {
                userService.update(
                        username,
                        body.get("password"),
                        body.get("newPassword"),
                        body.get("email")
                );

                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원 정보 수정에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.ACCEPTED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원 정보 수정에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "회원 정보 수정에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/user/{username}/profileImage", produces = MediaType.ALL_VALUE)
    @ResponseBody
    public FileSystemResource getProfileImage(@PathVariable("username") String username) throws IOException {
        String path = System.getProperty("user.dir") +
                String.format("\\src\\main\\resources\\static\\resource\\profile\\%s.jpg", username);

        return (new File(path).exists()) ? new FileSystemResource(path) : null;
    }

    @PostMapping("/api/user/{username}/profileImage")
    @ResponseBody
    public ResponseEntity changeProfileImage(@PathVariable("username") String username,
                                             @RequestParam("profileImage") String profileImage,
                                             @RequestParam(value="image", required = false) MultipartFile image) {
        if (!username.isBlank() && username.equals(Security.getCurrentUsername()) &&
                !profileImage.isBlank()) {
            try {
                userService.changeProfileImage(username, profileImage, image);

                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "프로필 이미지 수정에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.ACCEPTED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "프로필 이미지 수정에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "프로필 이미지 수정에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/user/{username}")
    public ResponseEntity deleteUser(@PathVariable("username") String username) {
        if (!username.isBlank() && username.equals(Security.getCurrentUsername())) {
            try {
                userService.delete(username);

                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원 정보 삭제에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.ACCEPTED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원 정보 삭제에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "회원 정보 삭제에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/checkforduplicate")
    @ResponseBody
    public ResponseEntity CanUseAsUsername(@RequestBody Map<String, String> body) {
        if (body.get("username") != null && !body.get("username").isBlank() &&
            userService.canUseAsUsername(body.get("username"))) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", true);
            return new ResponseEntity(result, HttpStatus.OK);
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", false);
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    @PostMapping("/api/findusername")
    @ResponseBody
    public ResponseEntity findUsername(@RequestBody Map<String, String> body) {
        try {
            if (body.get("email") == null || body.get("email").isBlank())
                throw new Exception();

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", userService.findUsernameByEmail(body.get("email")));
            return new ResponseEntity(result, HttpStatus.OK);
        }
        catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "");
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    private Cookie createTokenCookie(String token, int age) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        return cookie;
    }
}
