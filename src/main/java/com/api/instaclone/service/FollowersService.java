package com.api.instaclone.service;

import java.util.List;

import org.springframework.amqp.core.Message;

import com.api.instaclone.entity.Followers;
import com.api.instaclone.entity.Userinfo;

public interface FollowersService {
    void updateFollowers(Followers Followers);
    void deleteFollowers(Followers followers);
    List<Userinfo> getAllFollowersById(int id); 
    List<Userinfo> getAllFollowingById(int id);
    void sendFollowMessageToQueue(Followers followers,String operation);
    void receiveFollowMessageAndPerformAction(Message message);
}