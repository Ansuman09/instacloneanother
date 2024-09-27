package com.api.instaclone.service;

import java.util.List;

import com.api.instaclone.entity.PostImage;

public interface PostImageService {
    void addPostImage(int post_id,String imageName);
    List<PostImage> getPostImagesByOwner(String username);
    PostImage getPostImageByPostId(int id);
}
