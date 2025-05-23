package com.api.instaclone.service;

import java.util.List;

import com.api.instaclone.entity.Userinfo;

public interface UserInfoService {
    Userinfo getUserinfo(int id);
    List<Userinfo> getSearchedUserinfo(String searchString,int visiotorId);
    Userinfo getUserinfo(String name,int id);
    Userinfo getUserinfoImageByName(String username);
    void updateUserinfoUpdateName(Userinfo user,String username);
    void updateUserinfoUpdateImage(String username,String imageName);
    List<Userinfo> getTrendingUsers();
}
