package com.api.instaclone.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.Action;
import com.api.instaclone.service.ActionService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/action")
public class ActionController {
    
    @Autowired
    ActionService actionService;

    @GetMapping("/all")
    public ResponseEntity<List<Action>> getAllActions() {
        List<Action> actions=actionService.getAllActions();
        System.out.println("requesteed actions");
        return new ResponseEntity<>(actions,HttpStatus.OK);
        
    }
    
    @DeleteMapping("/delete/{post_id}/{user_id}")
    public ResponseEntity<HttpStatus> postMethodName(@PathVariable int post_id,@PathVariable int user_id) {
        actionService.deleteAction(post_id, user_id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/add/{user_id}")
    public ResponseEntity<Action> postMethodName(@PathVariable int user_id,@RequestBody Action action) {
        //add user to action
        action.setUser_id(user_id);
        Action newAction= actionService.addAction(action);
        return new ResponseEntity<>(newAction,HttpStatus.CREATED);
    }
    
    
}
