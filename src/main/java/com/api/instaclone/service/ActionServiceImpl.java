package com.api.instaclone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.Action;
import com.api.instaclone.repository.ActionRepository;


@Service
public class ActionServiceImpl implements ActionService{
    
    @Autowired
    ActionRepository actionRepository;
    
    @Override
    public List<Action> getAllActions() {
        return actionRepository.getAllActions();
    }

    @Override
    public void deleteAction(int post_id, int user_id) {
        actionRepository.deleteAction(post_id, user_id);
    }

    @Override
    public Action addAction(Action action) {
        Action newAction = actionRepository.addAction(action);
        return newAction;
    }

    @Override
    public List<Action> getActionByPostId(int id) {
        List<Action> listOfActions = actionRepository.getActionByPost(id);
        return listOfActions;
    }

    @Override
    public Boolean userHasLikedPost(int userId, int postId) {
        return actionRepository.userHasLikedPost(userId, postId);
    }
}
