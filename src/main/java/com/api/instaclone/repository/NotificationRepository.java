package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.api.instaclone.entity.Notification;
import com.api.instaclone.entity.Post;
import com.api.instaclone.entity.Userinfo;

@Repository
public class NotificationRepository {

    @Value("${myapp.deployment.backend.sql}")
    private String JdbcURL;

    private String username = "root";
    private String password = "qwerty11";

    Connection connection = null;

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }

    public Userinfo getUserinfo(int id) {
        Userinfo userinfo = new Userinfo();
        String sql = "SELECT * FROM userinfo WHERE userid=?";
        try {

            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer userid = resultSet.getInt("userid");
                String username = resultSet.getString("username");
                String profileimage = resultSet.getString("profile_image");

                userinfo = new Userinfo(userid, username, profileimage);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userinfo;
    }

    public Post getPostById(int cur_post_id) {

        String sql = "SELECT * FROM posts WHERE post_id=?";

        try {

            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cur_post_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int post_id = resultSet.getInt("post_id");
                int owner_id = resultSet.getInt("owner_id");
                String description = resultSet.getString("description");
                Userinfo userinfo = getUserinfo(owner_id);

                return new Post(post_id, owner_id, description, userinfo);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addNotification(Notification notification) {
        String sql = "INSERT INTO notification_table (uname, action, count, actinguser, message, post_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        if (notification.getAction().equals("like") || notification.getAction().equals("comment")) {
            Post post = getPostById(notification.getPost_id());
            notification.setUname(post.getUserinfo().getUsername());
        } else {
            notification.setPost_id(45);
        }

        String messageStatement = "notification";

        if (notification.getAction().equals("like")) {
            messageStatement = String.format(" liked your recent post");
        } else if (notification.getAction().equals("comment")) {
            messageStatement = String.format(" commented on your recent post");

        } else if (notification.getAction().equals("follow")) {
            messageStatement = String.format(" started following you");
        }

        notification.setMessage(messageStatement);
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, notification.getUname());
            preparedStatement.setString(2, notification.getAction());
            preparedStatement.setInt(3, notification.getCount());
            preparedStatement.setString(4, notification.getActinguser());
            preparedStatement.setString(5, notification.getMessage());
            preparedStatement.setInt(6, notification.getPost_id());
            preparedStatement.setString(7, "unread");

            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Notification> getNotificationByUserName(String username) {
        String sql = "SELECT * FROM notification_table WHERE uname=?";
        ArrayList<Notification> notifications = new ArrayList<>();
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Notification notification = new Notification();
                notification.setUname(resultSet.getString("uname"));
                notification.setId(resultSet.getInt("id"));
                notification.setActinguser(resultSet.getString("actinguser"));
                notification.setMessage(resultSet.getString("message"));
                notification.setStatus(resultSet.getString("status"));
                notifications.add(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public void updateNotification(Notification notification){
        String str = "UPDATE `notification_table` SET status=? WHERE id=?";
        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            preparedStatement.setString(1, "read");
            preparedStatement.setInt(2, notification.getId());
    
            preparedStatement.executeUpdate();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
