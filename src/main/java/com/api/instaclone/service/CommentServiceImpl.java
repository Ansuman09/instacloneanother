package com.api.instaclone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.Comment;
import com.api.instaclone.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService{
    
    @Autowired
    CommentRepository commentRepository;

    @Override
    public void addComment(Comment comment) {
        commentRepository.addComment(comment);
    }

    @Override
    public List<Comment> getCommentByPost(int id) {
        List<Comment> comments = commentRepository.getCommentByPost(id); 
        return comments;
    }

    @Override
    public Comment updateCommentById(String new_comment, int id) {
        commentRepository.updateCommentByCommentId(new_comment, id);
        return null;
    }

    @Override
    public void deleteCommentById(int id) {
        commentRepository.deleteCommentById(id);
    }
}
