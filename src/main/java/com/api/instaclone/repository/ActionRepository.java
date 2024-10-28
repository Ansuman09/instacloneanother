package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// import org.apache.tomcat.util.openssl.pem_password_cb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import com.api.instaclone.entity.Action;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.service.UserInfoService;

@Repository
public class ActionRepository {
    
    @Value("${myapp.deployment.backend.sql}")
    private String JdbcURL;
    
    private String username = "root";
    private String password = "qwerty11";

    Connection connection = null;

    @Autowired
    UserInfoService userInfoService;

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }

    public List<Action> getAllActions(){
        String sql = "SELECT * FROM action";
        List<Action> actions= new ArrayList<>();
        try (Connection connection = connect()){
                PreparedStatement prepareStatement = connect().prepareStatement(sql);
                ResultSet resultSet = prepareStatement.executeQuery();

                while (resultSet.next()) {
                    int user_id = resultSet.getInt("user_id");
                    int post_id = resultSet.getInt("post_id");
                    String action_type = resultSet.getString("action");


                    Action action = new Action(post_id,user_id,action_type);

                    actions.add(action);
                }
        connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return actions;
    } 

    public List<Action> getActionByPost(int id){
        String sql="SELECT * FROM action WHERE post_id=?";
        
        List<Action> listOfActionsOnPost=new ArrayList<Action>();
        try{
            Connection connection = connect();
            PreparedStatement preparedStatement=connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()) {
                int userId=resultSet.getInt("user_id");
                int postId=resultSet.getInt("post_id");
                String actionType = resultSet.getString("action");
                Userinfo userInfo =userInfoService.getUserinfo(userId);

                Action action = new Action(postId,actionType,userInfo);
                listOfActionsOnPost.add(action);
            } 
        connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return listOfActionsOnPost;
    };

    public Boolean userHasLikedPost(int userId,int postId){
        String sql = "SELECT * FROM action WHERE user_id=? and post_id=?";
        Boolean hasLiked=false;
        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, postId);

            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println(resultSet);           
            if (resultSet.next()){
                hasLiked=true;
            }
        connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return hasLiked;
    }

    public void deleteAction(int post_id,int user_id){
        String sql = "DELETE FROM action WHERE post_id=? and user_id=?";
        
        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, post_id);
            preparedStatement.setInt(2, user_id);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Action addAction(Action action){
        String sql = "INSERT INTO action VALUES(?,?,?)";

        try {
            Connection connection =connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, action.getPost_id());
            preparedStatement.setInt(2,action.getUser_id());
            preparedStatement.setString(3, action.getAction());

            preparedStatement.executeUpdate();
        connection.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return action;
    }
}
