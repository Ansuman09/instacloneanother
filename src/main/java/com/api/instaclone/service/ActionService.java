package com.api.instaclone.service;

import java.util.List;

import org.springframework.amqp.core.Message;

import com.api.instaclone.entity.Action;



public interface ActionService {
    List<Action> getAllActions();    
    void deleteAction(int post_id,int user_id);
    Action addAction(Action action);
    List<Action> getActionByPostId(int id);
    Boolean userHasLikedPost(int userId,int postId);
    void messageSender(Action action,String task);
    void messageReceiver(Message message);
} 
