package com.example.AuthFlow.Repository;

import com.example.AuthFlow.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    boolean existsById(Long id);
    Optional<User> findByName(String name);
    boolean existsByName(String name);
}
