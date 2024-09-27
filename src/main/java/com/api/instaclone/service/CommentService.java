package com.api.instaclone.service;

import java.util.List;

import com.api.instaclone.entity.Comment;

public interface CommentService {
    void addComment(Comment comment);
    List<Comment> getCommentByPost(int id);
    Comment updateCommentById(String new_comment,int id);
    void deleteCommentById(int id);
}
