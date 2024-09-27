package com.api.instaclone.service;

import com.api.instaclone.entity.User;

public interface UserService {
    public User getUser(String username);
    public User getUserid(String username);
    public void addUser(User user);
}
