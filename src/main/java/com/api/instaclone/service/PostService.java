package com.api.instaclone.service;

import java.util.List;

import com.api.instaclone.entity.Post;

public interface PostService {
    List<Post> getAllPosts(); 
    List<Post> getFollowingPosts(int id);    
    void addPost(Post post,String imageName);
    List<Post> getHomePagePosts(int userProfileId,int visitorId);
} 
