package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

// import org.springframework.boot.autoconfigure.cassandra.CassandraProperties.Connection;

// import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Cache.Connection;

import com.api.instaclone.entity.Userinfo;

@Repository
public class UserInfoRepository {
    
    private String JdbcURL = "jdbc:mysql://172.17.0.2:3306/test";
    private String username = "root";
    private String password = "qwerty11";

    public Connection connect() throws SQLException{
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }

    public Userinfo getUserinfo(int id){
        Userinfo userinfo = new Userinfo();
        String sql = "SELECT * FROM userinfo WHERE userid=?";
        try {
            
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet= preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer userid = resultSet.getInt("userid");
                String username = resultSet.getString("username");
                String profileimage= resultSet.getString("profile_image");
                
                userinfo = new Userinfo(userid,username,profileimage);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userinfo;
    }


    public Userinfo getUserinfoByName(String name){
        Userinfo userinfo = new Userinfo();
        String sql = "SELECT * FROM userinfo WHERE username=?";
        try {
            
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet= preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer userid = resultSet.getInt("userid");
                String username = resultSet.getString("username");
                String profileimage= resultSet.getString("profile_image");
                
                userinfo = new Userinfo(userid,username,profileimage);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userinfo;
    }


    public Userinfo getUserinfo(String name, int visitorId){
        Userinfo userinfo = null; // Initialize as null
        String sql = "SELECT * FROM userinfo WHERE username=?";
        String checkfollowing = "SELECT * FROM followers WHERE usr_id=?";
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet= preparedStatement.executeQuery();
    
            PreparedStatement preparedStatementToCheckFollowing = connection.prepareStatement(checkfollowing);
            while (resultSet.next()) {
                int userid = resultSet.getInt("userid");
                String username = resultSet.getString("username");
                String profileimage = resultSet.getString("profile_image");
                
                boolean is_following = false; // Reset for each user
                
                preparedStatementToCheckFollowing.setInt(1, visitorId);
                ResultSet resultSetToCheckFollowing = preparedStatementToCheckFollowing.executeQuery();
    
                while (resultSetToCheckFollowing.next()) {
                    int following_id = resultSetToCheckFollowing.getInt("following_id");
                    if (following_id == userid) {
                        is_following = true;
                        break; // No need to check further if following relationship is found
                    }
                }
    
                userinfo = new Userinfo(userid, username, profileimage, is_following);
                resultSetToCheckFollowing.close();
            }
    
            resultSet.close();
            preparedStatement.close();
            preparedStatementToCheckFollowing.close();
            connection.close();
    
        } catch (SQLException e){
            e.printStackTrace();
        }
        return userinfo;
    }
    

    public List<Userinfo> getSearchedUserinfo(String searchString,int visitorId){

        String sql = "SELECT * FROM userinfo WHERE username LIKE ?";
        List<Userinfo> userinfos = new ArrayList<>();
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "%"+searchString+"%");
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String username=resultSet.getString("username");
                int user_id = resultSet.getInt("userid");
                String profile_image = resultSet.getString("profile_image");

                Userinfo userinfo = new Userinfo(user_id,username,profile_image);
                if (visitorId==user_id){
                    userinfo.setIs_following(true);
                }
                userinfos.add(userinfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return userinfos;
    }
}
