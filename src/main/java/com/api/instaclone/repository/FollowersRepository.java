package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.api.instaclone.entity.Followers;
import com.api.instaclone.entity.Userinfo;

@Repository
public class FollowersRepository {
        
    private String JdbcURL = "jdbc:mysql://172.17.0.2:3306/test";
    private String username = "root";
    private String password = "qwerty11";

    Connection connection = null;

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }


    public void updateFollowers(Followers followers){
        String sql = "INSERT INTO followers VALUES(?,?)";

        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            System.out.printf("%d,%d",followers.getFollowing_id(),followers.getUsr_id());
            preparedStatement.setInt(1, followers.getFollowing_id());
            preparedStatement.setInt(2, followers.getUsr_id());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteFollowers(Followers followers){
        String sql = "DELETE FROM followers WHERE following_id=? AND usr_id=?";

        try {
            Connection connection = connect();
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, followers.getFollowing_id());
            preparedStatement.setInt(2, followers.getUsr_id());

            preparedStatement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<Userinfo> getFollowersById(int id){
        String sql="SELECT * FROM userinfo WHERE userid IN (SELECT usr_id FROM followers where following_id=?)";        
        List<Userinfo> users= new ArrayList<>();
        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, id);
            ResultSet resultSet= preparedStatement.executeQuery();

            while (resultSet.next()){
                int userid=resultSet.getInt("userid");
                String username=resultSet.getString("username");
                String profile_image=resultSet.getString("profile_image");

                Userinfo user= new Userinfo(userid, username, profile_image);
                users.add(user);
                // System.out.println("\n executed to add follower");
            }
        
        }catch(SQLException e){
            e.printStackTrace();
        }

        return users;
    }

    public List<Userinfo> getFollowingById(int id){
        String sql="SELECT * FROM userinfo WHERE userid IN (SELECT following_id FROM followers where usr_id=?)";        
        List<Userinfo> users= new ArrayList<>();
        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, id);
            ResultSet resultSet= preparedStatement.executeQuery();

            while (resultSet.next()){
                int userid=resultSet.getInt("userid");
                String username=resultSet.getString("username");
                String profile_image=resultSet.getString("profile_image");

                Userinfo user= new Userinfo(userid, username, profile_image);
                users.add(user);
                // System.out.println("\n executed to add follower");
            }
        
        }catch(SQLException e){
            e.printStackTrace();
        }

        return users;
    }

}
