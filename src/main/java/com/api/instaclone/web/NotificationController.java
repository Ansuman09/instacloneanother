package com.api.instaclone.web;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.api.instaclone.entity.Notification;
import com.api.instaclone.entity.User;
import com.api.instaclone.service.NotificationService;
import com.api.instaclone.service.UserService;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    private User getUserDetails(){
        String username=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getUser(username);
        return user;
    } 

    @GetMapping("/user")
    public ResponseEntity<ArrayList<Notification>> getNotifications() {
        User user = getUserDetails();
        ArrayList<Notification> notifications= notificationService.getNotificationByUsername(user.getUsername());

        return new ResponseEntity<>(notifications,HttpStatus.OK);
    }

    @PostMapping("/read/{id}")
    public ResponseEntity changeMessageToRead(@PathVariable Integer id) {
        Notification notification= new Notification();
        notification.setId(id);
        User user = getUserDetails();
        notificationService.messageSender(notification,"readNotifications");

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping("/delete/follow-request")
    public ResponseEntity<String> deleteFollowRequest(@RequestBody Notification notification) {
        notificationService.deleteNotificationByID(notification);
        String responseStr = new String().format("follow request rejected for %s",notification.getActinguser());
        return new ResponseEntity<>(responseStr,HttpStatus.OK);
    }

}
