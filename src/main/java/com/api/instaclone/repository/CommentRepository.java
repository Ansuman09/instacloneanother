package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.api.instaclone.entity.Comment;
import com.api.instaclone.entity.Userinfo;

@Repository
public class CommentRepository {

    @Autowired
    UserInfoRepository userInfoRepository;
    
    @Value("${myapp.deployment.backend.sql}")
    private String JdbcURL;
    
    private String username = "root";
    private String password = "qwerty11";

    Connection connection = null;

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }

    public void addComment(Comment comment){
        String sql = "INSERT INTO comments(post_id,usr_id,comment) VALUES(?,?,?)";

        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, comment.getPost_id());
            preparedStatement.setInt(2,comment.getUsr_id());
            preparedStatement.setString(3, comment.getComment());
            preparedStatement.execute();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public List<Comment> getCommentByPost (int id){
        List<Comment> comments = new ArrayList<>();
        String sql="SELECT * FROM comments WHERE post_id=?";
        System.err.println("getting comments");
        try{
        Connection connection=connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int comment_id = resultSet.getInt("comment_id");
            int post_id=resultSet.getInt("post_id");
            int usr_id=resultSet.getInt("usr_id");
            String statement = resultSet.getString("comment");
            Userinfo userinfo = userInfoRepository.getUserinfo(usr_id);
            Comment comment = new Comment(comment_id,post_id,usr_id,statement,userinfo);
            comments.add(comment);
        }
        connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return comments;
    }

    public Comment updateCommentByCommentId(String new_comment,int id){
        String sql = "UPDATE comments  SET comment=? WHERE comment_id=?";
        Comment comment=new Comment();

        try{
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, new_comment);
        preparedStatement.setInt(2, id);
        System.out.println(new_comment);

        preparedStatement.executeUpdate();
        comment=new Comment(id,new_comment);
        connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return comment;
    }

    public void deleteCommentById (int id){
        String sql = "DELETE from comments WHERE comment_id=?";

        try{
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            preparedStatement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}