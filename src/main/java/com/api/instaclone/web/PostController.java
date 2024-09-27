package com.api.instaclone.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.instaclone.entity.Post;
import com.api.instaclone.service.PostService;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/posts")
public class PostController {
    private static final String UPLOAD_DIR = "/home/ansuman/Documents/InstaImageBucket";

    @Autowired
    PostService postService;

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    
    @GetMapping("/feed/{id}")
    public ResponseEntity<List<Post>> getFollowingPosts(@PathVariable int id) {
        List<Post> posts = postService.getFollowingPosts(id);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @GetMapping("/home/{userProfileId}/{visitorId}")
    public ResponseEntity<List<Post>> getHomePagePosts(@PathVariable int userProfileId,@PathVariable int visitorId){
        List<Post> posts = postService.getHomePagePosts(userProfileId,visitorId);
        System.out.println("sending comments with post");
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    
    

    @PostMapping("/addpost")
public ResponseEntity<HttpStatus> postAddPost(
    @RequestParam("owner_id") int owner_id,
    @RequestParam("description") String description,
    @RequestPart("image") MultipartFile imageFile) {

    System.out.println("Got image");
    File directory = new File(UPLOAD_DIR);

    if (!directory.exists()) {
        directory.mkdirs();
    }

    String originalFileName = imageFile.getOriginalFilename();
    String newFileName = System.currentTimeMillis() + "_" + originalFileName;
    Path filePath = Paths.get(UPLOAD_DIR, newFileName);


    try {
        Files.copy(imageFile.getInputStream(), filePath);
        Post post = new Post(owner_id, description);
        postService.addPost(post,newFileName);

    } catch (IOException e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
}

    
}
