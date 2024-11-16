package com.api.instaclone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.Post;
import com.api.instaclone.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService{
    
    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.getAllPosts();
    }

    @Override
    public List<Post> getFollowingPosts(int id) {
        System.err.println("called repository");
        return postRepository.getFollowingPosts(id);
    }

    @Override
    public void addPost(Post post,String imageName) {
        postRepository.addPost(post,imageName);
    }

    @Override
    public List<Post> getHomePagePosts(int userProfileId,int visitorId) {
        return postRepository.getHomePagePosts(userProfileId,visitorId);
    }

    @Override
    public List<Post> getPostsBySearchString(String searchStr) {
        
        return postRepository.getPostsBySearchingDescripition(searchStr);
    }

    @Override
    public void deletePost(int post_id) {
        postRepository.deletePost(post_id);
    }
}
