package com.api.instaclone.service;

import java.util.List;

import com.api.instaclone.entity.User;
import com.api.instaclone.entity.Userinfo;

public interface UserInfoService {
    Userinfo getUserinfo(int id);
    Userinfo getUserinfoByName(String username);
    List<Userinfo> getSearchedUserinfo(String searchString,int visiotorId);
    Userinfo getUserinfo(String name,User user);
    Userinfo getUserinfoImageByName(String username);
    void updateUserinfoUpdateName(Userinfo user,String username);
    void updateUserinfoUpdateImage(String username,String imageName);
    void toggleUserAccountPrivateByName(String username,String privateAccount);
    List<Userinfo> getTrendingUsers();
}
