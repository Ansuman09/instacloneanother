package com.api.instaclone.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.User;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.service.UserInfoServiceImpl;
import com.api.instaclone.service.UserService;
import com.api.instaclone.service.UserServiceImpl;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;







@RestController
@RequestMapping("/userinfo")
public class UserinfoController {
    
    @Autowired
    UserInfoServiceImpl userInfoServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserService userService;

    public final String getUserNameFromJWT(){
        String name = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return name;
    }

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
    @GetMapping("/all/{searchString}")
    public ResponseEntity<List<Userinfo>> getSearchedUserinfo(@PathVariable String searchString){
        User visitorId = userService.getUserid(searchString);
        List<Userinfo> userinfos = userInfoServiceImpl.getSearchedUserinfo(searchString,visitorId.getUsr_id());
        return new ResponseEntity<>(userinfos,HttpStatus.OK);
    }
    
    @GetMapping("/{name}/{id}")
    public ResponseEntity<Userinfo> getUserInfo(@PathVariable String name,@PathVariable int id) {
        Userinfo userinfo = userInfoServiceImpl.getUserinfo(name,id);
        return new ResponseEntity<>(userinfo,HttpStatus.OK);
    }

    @GetMapping("/image")
    public ResponseEntity<Userinfo> getMethodName() {
        String username=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        //gets userimage file name and username
        Userinfo visitorUserinfo=userInfoServiceImpl.getUserinfoImageByName(username);
        return new ResponseEntity<>(visitorUserinfo, HttpStatus.OK);
    }
    
    @PostMapping("/update/name")
    public ResponseEntity<HttpStatus> updateUserName(@RequestBody Userinfo user) {
        String username=getUserNameFromJWT();        
        userInfoServiceImpl.updateUserinfoUpdateName(user, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
