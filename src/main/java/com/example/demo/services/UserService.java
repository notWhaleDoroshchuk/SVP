package com.example.demo.service;

import com.example.demo.dao.UsersRepository;
import com.example.demo.dto.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private UsersRepository usersRepository;

    public Optional<User> getUser(UUID id) {
        return usersRepository.findById(id);
    }

    public Optional<User> getUser(String login) {
        return usersRepository.findFirstByLogin(login);
    }

    public void save(User user) {
        usersRepository.save(user);
    }

}
