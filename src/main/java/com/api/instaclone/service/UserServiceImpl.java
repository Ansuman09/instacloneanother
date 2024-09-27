package com.api.instaclone.service;

// import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.User;
import com.api.instaclone.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    UserRepository userRepository;

    @Override
    public User getUser(String name) {
        return userRepository.getUser(name);
    }

    @Override
    public User getUserid(String username) {
        return userRepository.getUserid(username);
    }

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
    }
}
