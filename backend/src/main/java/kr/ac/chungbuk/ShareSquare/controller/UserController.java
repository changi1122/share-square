package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch(Exception e) {
            Cookie tokenCookie = createTokenCookie(null, 0);
            res.addCookie(tokenCookie);

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "아이디 또는 비밀번호가 잘못되었습니다.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/api/logout")
    public ResponseEntity logout(final HttpServletRequest req, final HttpServletResponse res) {
        Cookie tokenCookie = createTokenCookie(null, 0);
        res.addCookie(tokenCookie);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "로그아웃에 성공하였습니다.");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/api/register")
    public ResponseEntity createUser(@RequestBody Map<String, String> body) {

        if (body.get("username") != null && !body.get("username").isBlank() &&
                body.get("password") != null && !body.get("password").isBlank() &&
                body.get("email") != null && !body.get("email").isBlank()) {

            try {
                userService.create(
                        body.get("username"),
                        body.get("password"),
                        body.get("email")
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
        if (!username.isBlank() &&
                body.get("password") != null && !body.get("password").isBlank() &&
                body.get("email") != null && !body.get("email").isBlank()) {

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

    @DeleteMapping("/api/user/{username}")
    public ResponseEntity deleteUser(@PathVariable("username") String username) {
        if (!username.isBlank()) {
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


    private Cookie createTokenCookie(String token, int age) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        return cookie;
    }
}
