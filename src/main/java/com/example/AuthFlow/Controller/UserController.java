package com.example.AuthFlow.Controller;

import com.example.AuthFlow.Domain.User;
import com.example.AuthFlow.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("join")
    public String join() {
        return "member/membership";
    }


    @PostMapping("join")
    public String save(@RequestParam String id,
                       @RequestParam String name,
                       @RequestParam String pass) {
        userService.signup(id, name, pass);
        return "완료되었습니다."; // PRG 패턴
    }

    @GetMapping("login")
    public String login() {
        return "member/login";
    }

    @PostMapping("login")
    public String submit(@RequestParam String id,
                         @RequestParam String pass,
                         HttpSession session, RedirectAttributes re) {
        User user = userService.findById(id);
        if (user != null && user.getPass().equals(pass)) {
            System.out.println(id + " Login");
            session.setAttribute("user", user);
            re.addFlashAttribute("msg", id + "님 어서오세요.");
        }

            return "완료되었습니다.";
        }
}
