package com.example.demo.services;

import com.example.demo.dao.UsersRepository;
import com.example.demo.dto.User;
import com.example.demo.models.AuthUserRequest;
import com.example.demo.models.RegisterUserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;

    public ResponseEntity<String> registerUser(RegisterUserRequest request) {
        if(!validate(request.getEmail())) return new  ResponseEntity<>("Невалидный email", HttpStatus.BAD_REQUEST);
        if(usersRepository.findFirstByEmailOrLogin(request.getEmail(), request.getLogin()).isPresent())
            return  new ResponseEntity<>("Пользователь с таким логином или email уже существует", HttpStatus.BAD_REQUEST);

        var user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setLastVisitedAt(Instant.now());
        usersRepository.save(user);
        return  new ResponseEntity<>("Пользователь добавлен успешно", HttpStatus.OK);
    }
    public Optional<User> getUser(UUID id) {
        return usersRepository.findById(id);
    }

    public Optional<User> getUser(AuthUserRequest request) {
        return usersRepository.findFirstByLoginAndPassword(request.getLogin(), request.getPassword());
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

}
