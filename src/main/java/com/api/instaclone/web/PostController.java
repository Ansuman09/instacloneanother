package com.api.instaclone.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.instaclone.entity.Post;
import com.api.instaclone.service.PostService;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/admin/api")
    public ResponseEntity<String> getTestMethod() {
        Collection<String> authorities=SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .map(authority->authority.getAuthority().toString()).collect(Collectors.toList());

        boolean isAdmin=false;
        for (var authority:authorities){
            System.out.println(authority);
            if (authority.equals("ROLE_ADMIN")){
                System.out.println("confirmed user is admin");
                isAdmin=true;
            }
        }
        String statement="Hy this is admin";
        if (isAdmin){
        return new ResponseEntity<>(statement,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("you are not admin",HttpStatus.FORBIDDEN);
        }
    }
    

    
}
