package com.api.instaclone.web;

import org.springframework.web.bind.annotation.RestController;

import com.api.instaclone.entity.Comment;
import com.api.instaclone.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;




@CrossOrigin(origins = "http://localhost:3000",methods = {RequestMethod.PUT})
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // @CrossOrigin(origins = "http://localhost:3000",methods = {RequestMethod.PUT})
    @PostMapping("/edit_this_comment")
    public ResponseEntity<Comment> updateCommentById(@RequestBody Comment comment) {
        Comment newComment = commentService.updateCommentById(comment.getComment(),comment.getComment_id());
        // System.out.printf("Edited comment with comment id %d and comment is :: %s", newComment.getComment_id(),newComment.getComment());
        return new ResponseEntity<>(newComment,HttpStatus.ACCEPTED);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteCommentById(@PathVariable int id){
        commentService.deleteCommentById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }















}
