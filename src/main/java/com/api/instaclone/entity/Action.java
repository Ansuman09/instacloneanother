package com.api.instaclone.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Action {
    int post_id;
    int user_id;
    String action;   
    Userinfo userinfo;

    public Action(int post_id,String action){
        this.post_id=post_id;
        this.action=action;
    }

    public Action(String action){
        this.action=action;
    }

    public Action(int post_id,String action,Userinfo userinfo){
        this.post_id=post_id;
        this.action=action;
        this.userinfo=userinfo;
    }

    public Action(int user_id,int post_id,String action){
        this.post_id=post_id;
        this.action=action;
        this.user_id=user_id;
    }
}
