package com.api.instaclone.service;

import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.configs.RabbitConfig;
import com.api.instaclone.entity.Action;
import com.api.instaclone.entity.Comment;
import com.api.instaclone.repository.CommentRepository;
import com.api.instaclone.repository.SerializerDeserializer;

@Service
public class CommentServiceImpl implements CommentService{
    
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

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

    @Override
    public void sendCommentToQueue(Comment comment, String operation) {
        
        MessageProperties props=MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("operation", operation);

        
        try{
            byte[] messageBytes= SerializerDeserializer.serializeObject(comment);
            Message msg = new Message(messageBytes,props);
            rabbitTemplate.send(RabbitConfig.TOPIC_EXCHANGE,"clone.apricot.comments",msg);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

    @Override
    @RabbitListener(queues = "comments")
    public void receiveCommentAndPerformAction(Message message) {
        
        MessageProperties messageProperties= message.getMessageProperties();
        String operation=messageProperties.getHeader("operation");
        byte[] data = message.getBody();
        Comment comment = new Comment();
        try{
        
            comment = (Comment) SerializerDeserializer.deserializeObject(data);
            System.out.printf("User %d commented on  post with post id %d and header is %s",comment.getUsr_id(),comment.getPost_id(),operation);
            if (operation.equals("addComment")){
                this.addComment(comment);   
            }else if (operation.equals("editComment") ){
                commentRepository.updateCommentByCommentId(comment.getComment(),comment.getComment_id());
            }
            else if (operation.equals("deleteComment")){
                // this.deleteAction(action.getPost_id(),action.getUser_id());
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        
    }
}
