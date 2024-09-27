package com.api.instaclone.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.User;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.service.UserInfoServiceImpl;
import com.api.instaclone.service.UserServiceImpl;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;





@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/userinfo")
public class UserinfoController {
    
    @Autowired
    UserInfoServiceImpl userInfoServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @GetMapping("/{id}")
    public ResponseEntity<Userinfo> getUserInfo(@PathVariable int id) {
        Userinfo userinfo = userInfoServiceImpl.getUserinfo(id);
        return new ResponseEntity<>(userinfo,HttpStatus.OK);
    }
    
    @GetMapping("/getuserid/forlogin/{name}")
    public ResponseEntity<User> getUserid(@PathVariable String name) {
        User user = userServiceImpl.getUserid(name);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/getuserid/forHome/{name}")
    public ResponseEntity<User> getUserIdForHome(@PathVariable String name) {
        User user = userServiceImpl.getUserid(name);
        System.out.println("this is working");
        // System.out.printf("user id is %d",user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @GetMapping("/all/{searchString}/{visitorId}")
    public ResponseEntity<List<Userinfo>> getSearchedUserinfo(@PathVariable String searchString,@PathVariable int visitorId){
        System.out.println("called all userinfo");
        List<Userinfo> userinfos = userInfoServiceImpl.getSearchedUserinfo(searchString,visitorId);
        return new ResponseEntity<>(userinfos,HttpStatus.OK);
    }
    
    @GetMapping("/{name}/{id}")
    public ResponseEntity<Userinfo> getUserInfo(@PathVariable String name,@PathVariable int id) {
        Userinfo userinfo = userInfoServiceImpl.getUserinfo(name,id);
        return new ResponseEntity<>(userinfo,HttpStatus.OK);
    }
}
