package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.api.instaclone.entity.PostImage;
import com.api.instaclone.entity.User;
// import com.api.instaclone.service.UserInfoService;
import com.api.instaclone.service.UserService;

@Repository
public class PostImageRepository {
    
    private String JdbcURL = "jdbc:mysql://172.17.0.2:3306/test";
    private String username = "root";
    private String password = "qwerty11";

    Connection connection = null;

    @Autowired
    UserService userService;

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }

    public void addPostImage(int post_id,String imageName){
        String sql="INSERT INTO post_images VALUES(?,?)";
        
        try(Connection connection = connect();){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, post_id);
            preparedStatement.setString(1, imageName);

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<PostImage> getPostImagesByOwner(String username){

        List<PostImage> postImages=new ArrayList<>();
        User owner = userService.getUser(username);
        int owner_id = owner.getUsr_id();
        try {
            Connection connection = connect();
            String sql="SELECT * from post_images  JOIN posts ON post_images.post_id=posts.post_id WHERE posts.owner_id =?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, owner_id);

            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int post_id = resultSet.getInt("post_id");
                String imageName = resultSet.getString("image_name");
                PostImage postImage = new PostImage(post_id,imageName);
                postImages.add(postImage);

            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return postImages;
    }

    public PostImage getPostImageByPostId(int id){
        PostImage image=new PostImage();
        String sql ="SELECT * FROM post_images WHERE post_id=?";

        try {
            Connection connection = connect();
            PreparedStatement preparedStatementToGetImage= connection.prepareStatement(sql);
            preparedStatementToGetImage.setInt(1, id);

            ResultSet resultSet = preparedStatementToGetImage.executeQuery();
            while (resultSet.next()){
                int post_id = resultSet.getInt("post_id");
                String imageName = resultSet.getString("image_name");
                image= new PostImage(post_id,imageName);
                System.out.printf("Got image with post id %d  name %s", post_id,imageName);
            }
        connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }

        return image;
    }

}
