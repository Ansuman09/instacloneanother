package com.api.instaclone.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.Action;
import com.api.instaclone.entity.Comment;
import com.api.instaclone.entity.Notification;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.entity.Followers;

import com.api.instaclone.configs.RabbitConfig;
import com.api.instaclone.repository.CommentRepository;
import com.api.instaclone.repository.NotificationRepository;
import com.api.instaclone.repository.SerializerDeserializer;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    NotificationRepository notificationRepository; //newline

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    CommentService commentService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = "notifications")
    public void messageReceiver(Message message) {
        MessageProperties messageProperties= message.getMessageProperties();
        String operation=messageProperties.getHeader("operation");
        byte[] data = message.getBody();
        // Notification notification = new Notification();
        
        try{
            
            //Do this if there is a user like
            if (operation.equals("addLike")){
                Action action = (Action) SerializerDeserializer.deserializeObject(data);
                System.out.printf("User %d liked post wit post id %d and header is %s notification",action.getUser_id(),action.getPost_id(),operation);
           
                Userinfo user = userInfoService.getUserinfo(action.getUser_id());     
                Notification notification = new Notification(user.getUsername(), "like", 1, user.getUsername(), action.getPost_id(), "got 1 like");
                
                notificationRepository.addNotification(notification);
                
            } 
            //DO this if there is a comment 
            else if (operation.equals("addComment")){
                Comment comment = (Comment) SerializerDeserializer.deserializeObject(data);
                comment.getPost_id();

                Userinfo user = userInfoService.getUserinfo(comment.getUsr_id());
                Notification notification = new Notification(user.getUsername(), "comment", 1, user.getUsername(), comment.getPost_id(),"message");

                System.out.println("Captured comment\n");
                notificationRepository.addNotification(notification);

            }else if (operation.equals("follow")){
                Followers followers = (Followers) SerializerDeserializer.deserializeObject(data);
                
                Userinfo actingUser = userInfoService.getUserinfo(followers.getUsr_id());
                Userinfo user = userInfoService.getUserinfo(followers.getFollowing_id());
                
                System.out.printf("followers %d and %d",followers.getUsr_id(),followers.getFollowing_id());

                // Userinfo user = userInfoService.getUserinfo(comment.getUsr_id());
                Notification notification = new Notification(user.getUsername(), "follow", 1, actingUser.getUsername());
                
                // System.out.println("Captured comment\n");
                notificationRepository.addNotification(notification);

            }
        }catch(Exception e){
            e.printStackTrace();
        }


        
    }

    //sender notification Read
    @Override
    public void messageSender(Notification notification,String task) {
        MessageProperties props=MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("operation", task);

        
        try{
            byte[] messageBytes= SerializerDeserializer.serializeObject(notification);
            Message msg = new Message(messageBytes,props);
            rabbitTemplate.send(RabbitConfig.TOPIC_EXCHANGE,"clone.apricot.notifications",msg);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }


    //uncoment this and create a queue

    @Override
    @RabbitListener(queues = "notification-action")
    public void notificationUpdateMessageReceiver(Message message) {
        MessageProperties messageProperties= message.getMessageProperties();
        String operation=messageProperties.getHeader("operation");
        
        byte[] data = message.getBody();
        Notification notification = new Notification();
        try{
        
            notification = (Notification) SerializerDeserializer.deserializeObject(data);
            // System.out.printf("User %d liked post wit post id %d and header is %s\n",action.getUser_id(),action.getPost_id(),operation);
            if (operation.equals("readNotifications")){
                //** *
                //insert notification update code from repository
                notificationRepository.updateNotification(notification);
                System.out.println("Visitor read notId " + notification.getId());
                //** */   
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        
    }

    @Override
    public ArrayList<Notification> getNotificationByUsername(String username) {
        // TODO Auto-generated method stub
        return notificationRepository.getNotificationByUserName(username);
        
    }

}
