package com.api.instaclone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.Followers;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.repository.FollowersRepository;

@Service
public class FollowerServiceImpl implements FollowersService{
    
    @Autowired
    FollowersRepository followersRepository;
    
    @Override
    public void updateFollowers(Followers Followers) {
        followersRepository.updateFollowers(Followers);
    }

    @Override
    public void deleteFollowers(Followers followers) {
        followersRepository.deleteFollowers(followers);
    }

    @Override
    public List<Userinfo> getAllFollowersById(int id) {
        
        return followersRepository.getFollowersById(id);
    }

    @Override
    public List<Userinfo> getAllFollowingById(int id) {
        return followersRepository.getFollowingById(id);
    }

}
