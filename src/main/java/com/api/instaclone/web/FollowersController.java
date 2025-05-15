package com.api.instaclone.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.Followers;
import com.api.instaclone.entity.User;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.service.FollowersService;
import com.api.instaclone.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/followers")
public class FollowersController {
    
    @Autowired
    FollowersService followersService;

    @Autowired
    UserService userService;

    @PostMapping("add")
    public ResponseEntity<HttpStatus> updateFollowers (@RequestBody Followers followers) {
        String username=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User owner=userService.getUser(username);
        followers.setUsr_id(owner.getUsr_id());
        followersService.sendFollowMessageToQueue(followers, "follow");;
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("delete")
    public ResponseEntity<HttpStatus> deleteFollowers (@RequestBody Followers followers){
        String username=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User owner=userService.getUser(username);
        followers.setUsr_id(owner.getUsr_id());
        followersService.sendFollowMessageToQueue(followers, "unfollow");;
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/userfollowers")
    public ResponseEntity<List<Userinfo>> getFollowersById() {
        // System.err.println("\nexecuted to get followers");
        String username=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User owner=userService.getUser(username);
        List<Userinfo> users=followersService.getAllFollowersById(owner.getUsr_id());
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    
    @GetMapping("/following")
    public ResponseEntity<List<Userinfo>> getFollowinfById(){

        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User owner=userService.getUser(username);
        List<Userinfo> users= followersService.getAllFollowingById(owner.getUsr_id());
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/userfollowers/{username}")
    public ResponseEntity<List<Userinfo>> getFollowersById(@PathVariable String username) {
        // System.err.println("\nexecuted to get followers");
        User owner=userService.getUser(username);
        List<Userinfo> users=followersService.getAllFollowersById(owner.getUsr_id());
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    
    @GetMapping("/following/{username}")
    public ResponseEntity<List<Userinfo>> getFollowinfById(@PathVariable String username){
        
        User owner=userService.getUser(username);
        List<Userinfo> users= followersService.getAllFollowingById(owner.getUsr_id());
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    
}
