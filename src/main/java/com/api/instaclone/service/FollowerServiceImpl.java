package com.api.instaclone.service;

import java.util.List;

import org.apache.tomcat.util.digester.SystemPropertySource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.instaclone.configs.RabbitConfig;
import com.api.instaclone.entity.Comment;
import com.api.instaclone.entity.Followers;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.repository.FollowersRepository;
import com.api.instaclone.repository.SerializerDeserializer;

@Service
public class FollowerServiceImpl implements FollowersService{
    
    @Autowired
    FollowersRepository followersRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;
    
    @Override
    public void updateFollowers(Followers Followers) {
        followersRepository.updateFollowers(Followers);
    }

    @Override
    public void deleteFollowers(Followers followers) {
        followersRepository.deleteFollowers(followers);
    }

    @Override
    public List<Userinfo> getAllFollowersById(int id) {
        
        return followersRepository.getFollowersById(id);
    }

    @Override
    public List<Userinfo> getAllFollowingById(int id) {
        return followersRepository.getFollowingById(id);
    }


    @Override
    public void sendFollowMessageToQueue(Followers followers, String operation) {
        
        MessageProperties props=MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("operation", operation);

        
        try{
            byte[] messageBytes= SerializerDeserializer.serializeObject(followers);
            Message msg = new Message(messageBytes,props);
            rabbitTemplate.send(RabbitConfig.TOPIC_EXCHANGE,"clone.apricot.subscriptions",msg);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

    @Override
    @RabbitListener(queues =RabbitConfig.FOLLOWER_QUEUE_NAME)
    public void receiveFollowMessageAndPerformAction(Message message) {
        
        MessageProperties messageProperties= message.getMessageProperties();
        String operation=messageProperties.getHeader("operation");
        byte[] data = message.getBody();
        Followers followers = new Followers();
        try{
        
            followers = (Followers) SerializerDeserializer.deserializeObject(data);
            System.out.printf("User %d and %d %s",followers.getUsr_id(),followers.getFollowing_id(),operation);
            if (operation.equals("follow")){
                this.updateFollowers(followers);   
            }else if (operation.equals("unfollow")){
                this.deleteFollowers(followers);
            }else if (operation.equals("follow-request")){
                //Notification service processes follow
                System.out.printf("follow request sent to user %d from user %d\n",followers.getFollowing_id(),followers.getUsr_id());
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        
    }
}
