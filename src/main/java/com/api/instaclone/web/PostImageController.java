package com.api.instaclone.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.PostImage;
import com.api.instaclone.entity.User;
import com.api.instaclone.service.PostImageService;
import com.api.instaclone.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/postimages")
public class PostImageController {
    
    @Autowired
    PostImageService postImageService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<PostImage>> getPostsByOwner() {
        String ownerName=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        // User user = userService.getUser(ownerName);
        List<PostImage> postImage = postImageService.getPostImagesByOwner(ownerName);
        return new ResponseEntity<>(postImage,HttpStatus.OK);
    }
    
    @GetMapping("/all/{username}")
    public ResponseEntity<List<PostImage>> getPostsByOwnerUsingPath(@PathVariable String username) {
        // User user = userService.getUser(ownerName);
        List<PostImage> postImage = postImageService.getPostImagesByOwner(username);
        return new ResponseEntity<>(postImage,HttpStatus.OK);
    }

}
