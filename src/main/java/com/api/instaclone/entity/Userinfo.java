package com.api.instaclone.entity;

import java.io.Serializable;
import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Userinfo implements Serializable{
    int userid;
    String username;
    String profile_image;
    LocalDateTime created_date;
    Boolean is_following;
    String private_account;
    Boolean has_requested;

    public Userinfo(int id,String name,String image_url){
        this.userid=id;
        this.username=name;
        this.profile_image=image_url;
    }

    public Userinfo(int id,String name,String image_url,boolean is_following){
        this.userid=id;
        this.username=name;
        this.profile_image=image_url;
        this.is_following=is_following;
    }

    public Userinfo(int id,String name,String image_url,String private_account){
        this.userid=id;
        this.username=name;
        this.profile_image=image_url;
        this.private_account=private_account;
    }

    public Userinfo(int id,String name,String image_url,boolean is_following,String private_account){
        this.userid=id;
        this.username=name;
        this.profile_image=image_url;
        this.is_following=is_following;
        this.private_account=private_account;
    }

    public Userinfo(int id,String name,String image_url,boolean is_following,String private_account,Boolean has_requested){
        this.userid=id;
        this.username=name;
        this.profile_image=image_url;
        this.is_following=is_following;
        this.private_account=private_account;
        this.has_requested=has_requested;
    }


    public Userinfo(String name,String image_url){
        this.username=name;
        this.profile_image=image_url;
    }
}
