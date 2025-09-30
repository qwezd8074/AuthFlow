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
    public String save(@RequestParam Long id,
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
    public String submit(@RequestParam String name,
                         @RequestParam String pass,
                         HttpSession session, RedirectAttributes re) {
        Optional<User> optionalUser = userService.findByName(name);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPass().equals(pass)) {
                System.out.println(name + " Login");
                session.setAttribute("user", user);
                re.addFlashAttribute("msg", name + "님 어서오세요.");
                return "완료되었습니다.";
            }
        }

        return "실피했습니다.";
    }
}
