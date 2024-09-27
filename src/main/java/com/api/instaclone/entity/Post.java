package com.api.instaclone.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    int post_id;
    int owner_id;
    String description;
    LocalDateTime upload_date;
    LocalDateTime edited_date;
    Userinfo userinfo;
    PostImage image;
    List<Action> actions;
    List<Comment> comments;
    Boolean hasLiked;
    
    public Post(int post_id,int owner_id,String description,Userinfo userinfo){
        this.post_id=post_id;
        this.owner_id=owner_id;
        this.description=description;
        this.userinfo = userinfo;
    }

    public Post(int post_id,int owner_id,String description,Userinfo userinfo,Boolean hasLiked,List<Action> actions){
        this.post_id=post_id;
        this.owner_id=owner_id;
        this.description=description;
        this.userinfo = userinfo;
        this.hasLiked=hasLiked;
        this.actions=actions;
    }

    public Post(int post_id,int owner_id,String description,Userinfo userinfo,Boolean hasLiked,List<Action> actions,List<Comment> comments){
        this.post_id=post_id;
        this.owner_id=owner_id;
        this.description=description;
        this.userinfo = userinfo;
        this.hasLiked=hasLiked;
        this.actions=actions;
        this.comments=comments;
    }

    //post with image

    public Post(int post_id,int owner_id,PostImage image,String description,Userinfo userinfo,Boolean hasLiked,List<Action> actions,List<Comment> comments){
        this.post_id=post_id;
        this.owner_id=owner_id;
        this.description=description;
        this.image=image;
        this.userinfo = userinfo;
        this.hasLiked=hasLiked;
        this.actions=actions;
        this.comments=comments;
    }
    public Post(int post_id,int owner_id,String description){
        this.post_id=post_id;
        this.owner_id=owner_id;
        this.description=description;
        // this.upload_date=upload_date;
        
    }

    public Post(int owner_id,String description){
        this.owner_id=owner_id;
        this.description=description;
    }

    public int getPost_id(){
        return this.post_id;
    }
}
