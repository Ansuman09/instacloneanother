package com.api.instaclone.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.PostImage;
import com.api.instaclone.service.PostImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/postimages")
public class PostImageController {
    
    @Autowired
    PostImageService postImageService;

    @GetMapping("/all/{owner_name}")
    public ResponseEntity<List<PostImage>> getPostsByOwner(@PathVariable String owner_name) {
        List<PostImage> postImage = postImageService.getPostImagesByOwner(owner_name);
        return new ResponseEntity<>(postImage,HttpStatus.OK);
    }
    

}
