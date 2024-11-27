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

import com.api.instaclone.entity.Action;
import com.api.instaclone.repository.ActionRepository;


@Service
public class ActionServiceImpl implements ActionService{
    
    @Autowired
    ActionRepository actionRepository;
    
    @Autowired
    RabbitTemplate rabbitTemplate;

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
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(action);
            byte[] messageBytes= byteArrayOutputStream.toByteArray();
            Message msg = new Message(messageBytes,props);

            objectOutputStream.close();
            rabbitTemplate.send("action",msg);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

    @Override
    @RabbitListener(queues = "action")
    public void messageReceiver(Message message) {
        MessageProperties messageProperties= message.getMessageProperties();
        String headerset=messageProperties.getHeader("operation");
        byte[] data = message.getBody();
        try{
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            Action action = (Action) objectInputStream.readObject();
            System.out.printf("User %s liked post wit post id %d and header is %s",action.getUsername(),action.getPost_id(),headerset);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
