package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import com.api.instaclone.entity.User;

@Repository
public class UserRepository {

    @Value("${myapp.deployment.backend.sql}")
    private String JdbcURL;
    
    private String username = "root";
    private String password = "qwerty11";

    Connection connection = null;

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }

    @Autowired
    User user;

    public User getUser(String name){
        String sql = "SELECT * FROM users WHERE username=?";
        User user=null;
        try{    
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Integer id = resultSet.getInt("usr_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                // String email = resultSet.getString("email");
                user = new User(id,username,password);
                
                System.out.println("Found the user: " +user.getUsername()+" "+user.getPassword());
            }
        
        connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    public User getUserid(String username){
        String sql = "SELECT * FROM users WHERE username=?";
        User user=new User();
        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                int userid= resultSet.getInt("usr_id");
                user = new User(userid);
            }
        connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    return user;
    }

    public void addUser(User user){
        String sql = "INSERT INTO users(username,email,password) VALUES(?,?,?)";
        System.out.printf("this registers user %s", user.getUsername());
        String newPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,newPassword);

            preparedStatement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    
}
