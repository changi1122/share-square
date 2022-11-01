package kr.ac.chungbuk.ShareSquare.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class TestController {

    @GetMapping("/api/currentuser")
    @ResponseBody
    public HashMap<String, Object> index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", authentication.getPrincipal());
        result.put("Authorities", authentication.getAuthorities());
        return result;
    }
}
