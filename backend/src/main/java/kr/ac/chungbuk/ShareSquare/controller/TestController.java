package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.utility.Security;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class TestController {

    @GetMapping("/api/currentuser")
    @ResponseBody
    public HashMap<String, Object> index() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", Security.getCurrentUsername());
        result.put("Authorities", Security.getCurrentUserRole());
        return result;
    }
}
