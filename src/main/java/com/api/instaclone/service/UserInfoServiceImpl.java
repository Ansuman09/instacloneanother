package com.api.instaclone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.repository.UserInfoRepository;

@Service
public class UserInfoServiceImpl implements UserInfoService{
    

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public Userinfo getUserinfo(int id) {
        return userInfoRepository.getUserinfo(id);
    }

    @Override
    public List<Userinfo> getSearchedUserinfo(String searchString,int visiotorId) {
        return userInfoRepository.getSearchedUserinfo(searchString,visiotorId);
    }

    @Override
    public Userinfo getUserinfo(String name, int id) {
        return userInfoRepository.getUserinfo(name,id);
    }

    @Override
    public Userinfo getUserinfoImageByName(String username) {
        return userInfoRepository.getUserinfoImageByName(username);
    }

    @Override
    public void updateUserinfoUpdateName(Userinfo user, String username) {
        userInfoRepository.updateUserinfoUpdateName(user, username);
    }
}
