package com.api.instaclone.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import com.api.instaclone.entity.Notification;
import com.api.instaclone.entity.Post;
import com.api.instaclone.entity.User;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.repository.ActionRepository;
import com.api.instaclone.repository.NotificationRepository;
import com.api.instaclone.repository.SerializerDeserializer;
import com.api.instaclone.repository.PostRepository;

@Service
public class ActionServiceImpl implements ActionService{
    
    @Autowired
    ActionRepository actionRepository;
    

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UserInfoService userInfoService; //new line


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

    @Override
    public void messageSender(Action action,String task) {
        MessageProperties props=MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("operation", task);

        
        try{
            byte[] messageBytes= SerializerDeserializer.serializeObject(action);
            Message msg = new Message(messageBytes,props);
            rabbitTemplate.send(RabbitConfig.TOPIC_EXCHANGE,"clone.apricot.actions",msg);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

    @Override
    @RabbitListener(queues = "actions")
    public void messageReceiver(Message message) {
        MessageProperties messageProperties= message.getMessageProperties();
        String operation=messageProperties.getHeader("operation");
        byte[] data = message.getBody();
        Action action = new Action();
        // Notification notification = new Notification();
        try{
        
            action = (Action) SerializerDeserializer.deserializeObject(data);
            System.out.printf("User %d liked post wit post id %d and header is %s\n",action.getUser_id(),action.getPost_id(),operation);
            if (operation.equals("addLike")){
                this.addAction(action);   
            }else if (operation.equals("unlike")){
                this.deleteAction(action.getPost_id(),action.getUser_id());
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        
    }
}
