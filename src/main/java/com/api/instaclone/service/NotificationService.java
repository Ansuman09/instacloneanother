package com.api.instaclone.service;

import java.util.ArrayList;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.Followers;
import com.api.instaclone.entity.Notification;

@Service
public interface NotificationService {
    public void messageReceiver(Message message);

    public ArrayList<Notification> getNotificationByUsername(String username);

    public void notificationUpdateMessageReceiver(Message message);

    public void messageSender(Notification notification, String task);

    public void deleteNotificationByID(Notification notification);
    public void deleteNotificationByUnameActionANDActingUser(Notification notification);
}
