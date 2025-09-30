package com.example.AuthFlow.Service;

import com.example.AuthFlow.Domain.User;
import com.example.AuthFlow.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id){
        Optional<User> u = userRepository.findById(id);
        return u;
    }

    public Optional<User> findByName(String name) {
        Optional<User> u = userRepository.findByName(name);
        return u;
    }

    @Transactional //회원가입
    public void  signup(Long id, String name, String pass){
        if(userRepository.existsById(id)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디 입니다.");
        }
        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setPass(pass);
        userRepository.save(u);
    }




}
