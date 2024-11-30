package com.api.instaclone.service;

import java.util.List;

import org.springframework.amqp.core.Message;

import com.api.instaclone.entity.Comment;

public interface CommentService {
    void addComment(Comment comment);
    List<Comment> getCommentByPost(int id);
    Comment updateCommentById(String new_comment,int id);
    void deleteCommentById(int id);

    void sendCommentToQueue(Comment comment,String operation);
    void receiveCommentAndPerformAction(Message message);
}
