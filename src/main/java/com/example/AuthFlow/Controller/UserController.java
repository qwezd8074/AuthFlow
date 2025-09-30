package com.example.AuthFlow.Controller;

import com.example.AuthFlow.Domain.User;
import com.example.AuthFlow.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("join")
    public String join() {
        return "member/membership";
    }


    @PostMapping("join")
    @ResponseBody()
    public ResponseEntity<String> save(@RequestParam Long id,
                       @RequestParam String name,
                       @RequestParam String pass) {
        userService.signup(id, name, pass);

        return new ResponseEntity<>("회원가입에 성공했습니다.", HttpStatus.OK);
    }

    @GetMapping("login")
    public String login() {
        return "member/login";
    }

    @PostMapping("login")
    @ResponseBody()
    public ResponseEntity<String> submit(@RequestParam String name,
                                 @RequestParam String pass,
                                 HttpSession session, RedirectAttributes re) {
        Optional<User> optionalUser = userService.findByName(name);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPass().equals(pass)) {
                System.out.println(name + " Login");
                session.setAttribute("user", user);
                re.addFlashAttribute("msg", name + "님 어서오세요.");
                return new ResponseEntity<>("로그인에 성공했습니다.", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("로그인에 실패했습니다.",HttpStatus.BAD_REQUEST);
    }
}
