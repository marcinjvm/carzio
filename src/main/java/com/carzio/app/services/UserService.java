package com.carzio.app.services;

import com.carzio.app.exceptions.ErrCode;
import com.carzio.app.models.User;
import com.carzio.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException(ErrCode.USER_OF_ID_NOT_FOUND.getMessage(id, Locale.ENGLISH)));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
