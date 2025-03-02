package com.api.instaclone.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.User;
import com.api.instaclone.service.RoleService;
import com.api.instaclone.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<String>  addUser(@RequestBody User user){
        String status = userService.addUser(user);
        roleService.addRole(user);
        if (status.equals("added")){
            return new ResponseEntity<>("Successfully registered",HttpStatus.CREATED);
        }else if(status.equals("exists")){
            return new ResponseEntity<>("Prexisting User",HttpStatus.CONFLICT);
        }
        else{
            return new ResponseEntity<>("Please try again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/change/role/{new_role}")
    public ResponseEntity<String> postMethodName(@RequestBody User user,@PathVariable String new_role) {
        roleService.updateRole(user,new_role);
        
        return new ResponseEntity<>("Changed user role",HttpStatus.OK);
    }
    
    
}
