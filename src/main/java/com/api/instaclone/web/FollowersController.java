package com.api.instaclone.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.Followers;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.service.FollowersService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/followers")
public class FollowersController {
    
    @Autowired
    FollowersService followersService;

    @PostMapping("add")
    public ResponseEntity<HttpStatus> updateFollowers (@RequestBody Followers followers) {
        followersService.updateFollowers(followers);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("delete")
    public ResponseEntity<HttpStatus> deleteFollowers (@RequestBody Followers followers){
        followersService.deleteFollowers(followers);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/userfollowers/{id}")
    public ResponseEntity<List<Userinfo>> getFollowersById(@PathVariable int id) {
        // System.err.println("\nexecuted to get followers");
        List<Userinfo> users=followersService.getAllFollowersById(id);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    
    @GetMapping("/following/{id}")
    public ResponseEntity<List<Userinfo>> getFollowinfById(@PathVariable int id){
        List<Userinfo> users= followersService.getAllFollowingById(id);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
}
