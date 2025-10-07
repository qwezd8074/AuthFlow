package com.example.AuthFlow.Controller;

import com.example.AuthFlow.Domain.User;
import com.example.AuthFlow.Repository.UserRepository;
import com.example.AuthFlow.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class UserController {

    private static final Pattern PW_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:;\"'`~<>,.?/]).{8,20}$");

    @PostMapping(value = "/validatePassword", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> validatePassword(@RequestParam String pass,
                                                   @RequestParam String confirm) {

        if (!pass.equals(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("비밀번호가 일치하지 않습니다.");
        }

        if (!PW_PATTERN.matcher(pass).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("8~20자, 영문/숫자/특수문자 포함으로 입력하세요.");
        }

        return ResponseEntity.ok("사용 가능한 비밀번호입니다.");
    }

    @Autowired
    UserService userService;

    @GetMapping("join")
    public String join() {
        return "member/membership";
    }

    @GetMapping("checkName")
    public ResponseEntity<String> checkName(@RequestParam String name) {
        boolean exists = userService.existsName(name);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 사용 중인 아이디입니다.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping(value = "join", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> save(@RequestParam String name,
                                       @RequestParam String pass) {
        try {
            userService.signup(name, pass);
            return ResponseEntity.ok("회원가입에 성공했습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
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
