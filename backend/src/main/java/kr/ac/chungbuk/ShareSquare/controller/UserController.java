package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping("/api/register")
    @ResponseBody
    public HashMap<String, Object> addUser(@RequestParam Map<String, String> body) {
        // TO-DO : 검증 과정 추가
        
        userService.create(
                body.get("username"),
                body.get("password"),
                body.get("email")
        );

        HashMap<String, Object> response = new HashMap<>();
        response.put("result", "회원가입에 성공하였습니다.");
        return response;
    }
}
