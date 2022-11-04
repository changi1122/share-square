package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public HashMap<String, Object> addUser(@RequestBody Map<String, String> body) {

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
                return result;
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원가입에 실패하였습니다.");
                return result;
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "회원가입에 실패하였습니다.");
            return result;
        }
    }

    @PostMapping("/api/checkforduplicate")
    @ResponseBody
    public HashMap<String, Object> CanUseAsUsername(@RequestBody Map<String, String> body) {
        if (body.get("username") != null && !body.get("username").isBlank() &&
            userService.canUseAsUsername(body.get("username"))) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", true);
            return result;
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", false);
            return result;
        }
    }

}
