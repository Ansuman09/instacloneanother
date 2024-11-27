package com.api.instaclone.entity;

import java.io.Serializable;

// import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Comment implements Serializable{
    int comment_id;
    int post_id;
    int usr_id;
    String comment;
    Userinfo userinfo;
    
    public Comment(int comment_id,int post_id,int usr_id,String comment){
        this.comment=comment;
        this.comment_id=comment_id;
        this.usr_id=usr_id;
        this.post_id=post_id;
    }


    public Comment(int comment_id,int post_id,int usr_id,String comment,Userinfo userinfo){
        this.comment=comment;
        this.comment_id=comment_id;
        this.usr_id=usr_id;
        this.post_id=post_id;
        this.userinfo=userinfo;
    }

    public Comment(int usr_id,int post_id,String comment){
        this.usr_id=usr_id;
        this.post_id=post_id;
        this.comment=comment;
    }

    public Comment(int comment_id,String comment){
        this.comment_id=comment_id;
        this.comment=comment;
    }
}
