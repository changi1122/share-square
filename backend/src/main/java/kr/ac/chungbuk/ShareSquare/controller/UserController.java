package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public HashMap<String, Object> login(final HttpServletRequest req,
                                         final HttpServletResponse res,
                                         @RequestBody Map<String, String> request) throws Exception {

        try {
            String token = userService.tryLogin(request.get("username"), request.get("password"));

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "로그인에 성공하였습니다.");
            result.put("token", token);
            return result;
        }
        catch(Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "아이디 또는 비밀번호가 잘못되었습니다.");
            return result;
        }
    }

    @PostMapping("/api/register")
    @ResponseBody
    public HashMap<String, Object> addUser(@RequestParam Map<String, String> body) {
        // TO-DO : 검증 과정 추가
        
        userService.create(
                body.get("username"),
                body.get("password"),
                body.get("email")
        );

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "회원가입에 성공하였습니다.");
        return result;
    }
}
