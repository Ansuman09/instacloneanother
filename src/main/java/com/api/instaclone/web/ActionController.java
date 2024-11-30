package com.api.instaclone.web;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.Action;
import com.api.instaclone.entity.User;
import com.api.instaclone.service.ActionService;
import com.api.instaclone.service.UserService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/action")
public class ActionController {
    
    @Autowired
    ActionService actionService;

    @Autowired
    UserService userService;

    public final User getUserInfoFromJWT(){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userService.getUserid(username);
    } 
    @GetMapping("/all")
    public ResponseEntity<List<Action>> getAllActions() {
       
        List<Action> actions=actionService.getAllActions();
        System.out.println("requesteed actions");
        return new ResponseEntity<>(actions,HttpStatus.OK);
        
    }
    
    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<HttpStatus> postMethodName(@PathVariable int post_id) {
        User owner = getUserInfoFromJWT();
        actionService.deleteAction(post_id, owner.getUsr_id());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/add")
    public ResponseEntity<Action> postMethodName(@RequestBody Action action) {
        //add user to action
        User owner = getUserInfoFromJWT();
        action.setUser_id(owner.getUsr_id());
        Action newAction= actionService.addAction(action);
        return new ResponseEntity<>(newAction,HttpStatus.CREATED);
    }
    
    @DeleteMapping("/delete/{post_id}/{user_id}")
    public ResponseEntity<HttpStatus> postMethodName(@PathVariable int post_id,@PathVariable int user_id) {
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
        if (isAdmin){
            actionService.deleteAction(post_id,user_id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }    

    @PostMapping("/queue/useraction/{operation}")
    public ResponseEntity<String> post(@RequestBody Action action,@PathVariable String operation) {
        User owner = getUserInfoFromJWT();
        action.setUser_id(owner.getUsr_id());
        actionService.messageSender(action,operation);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
