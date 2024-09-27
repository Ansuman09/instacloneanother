package com.api.instaclone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.PostImage;
import com.api.instaclone.repository.PostImageRepository;

@Service
public class PostImageServiceImpl implements PostImageService {
    
    @Autowired
    PostImageRepository postImageRepository;

    @Override
    public void addPostImage(int post_id, String imageName) {
        postImageRepository.addPostImage(post_id, imageName);
    }

    @Override
    public List<PostImage> getPostImagesByOwner(String username) {
        List<PostImage> postImages = postImageRepository.getPostImagesByOwner(username);
        return postImages; 
    }

    @Override
    public PostImage getPostImageByPostId(int id) {
        PostImage image = postImageRepository.getPostImageByPostId(id);
        return image;
    }    
}
