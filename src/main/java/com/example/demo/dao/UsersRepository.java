package com.example.demo.dao;

import com.example.demo.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {

    Optional<User> findFirstByLoginAndPassword(String login, String password);
    Optional<User> findFirstByEmailOrLogin(String email, String login);
}
