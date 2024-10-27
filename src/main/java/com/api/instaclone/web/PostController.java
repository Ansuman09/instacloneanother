package com.api.instaclone.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.instaclone.entity.Post;
import com.api.instaclone.entity.User;
import com.api.instaclone.service.PostService;
import com.api.instaclone.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/posts")
public class PostController {
    private static final String UPLOAD_DIR = "/home/ansuman/Documents/InstaImageBucket";

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    public final User getUserFromJWT(){
        String curUser=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user=userService.getUser(curUser);
        return user;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    
    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFollowingPosts() {
        User visitor=getUserFromJWT();
        List<Post> posts = postService.getFollowingPosts(visitor.getUsr_id());
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @GetMapping("/home")
    public ResponseEntity<List<Post>> getHomePagePosts(){
        User visitor=getUserFromJWT();
        List<Post> posts = postService.getHomePagePosts(visitor.getUsr_id(),visitor.getUsr_id());
        System.out.println("sending comments with post");
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @GetMapping("/home/{username}")
    public ResponseEntity<List<Post>> getHomePagePosts(@PathVariable String username){
        User visitor=getUserFromJWT();
        User profileUser=userService.getUserid(username);
        List<Post> posts = postService.getHomePagePosts(profileUser.getUsr_id(),visitor.getUsr_id());
        System.out.println("sending comments with post");
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    
    @GetMapping("/home/search/{postquery}")
    public ResponseEntity<List<Post>> getSearchResultByPostDescripition(@PathVariable String postquery) {
        List<Post> posts=postService.getPostsBySearchString(postquery);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    
    

    @PostMapping("/addpost")
    public ResponseEntity<HttpStatus> postAddPost(
        @RequestParam("description") String description,
        @RequestPart("image") MultipartFile imageFile) {
        
        String principalUser=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println("Got image");
        File directory = new File(UPLOAD_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFileName = imageFile.getOriginalFilename();
        String newFileName = System.currentTimeMillis() + "_" + originalFileName;
        Path filePath = Paths.get(UPLOAD_DIR, newFileName);

        User owner=userService.getUser(principalUser);

        try {
            Files.copy(imageFile.getInputStream(), filePath);
            Post post = new Post(owner.getUsr_id(), description);
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
