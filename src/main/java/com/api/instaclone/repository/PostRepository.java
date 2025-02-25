package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.api.instaclone.entity.Action;
import com.api.instaclone.entity.Comment;
import com.api.instaclone.entity.Post;
import com.api.instaclone.entity.PostImage;
import com.api.instaclone.entity.Userinfo;
import com.api.instaclone.service.ActionService;
import com.api.instaclone.service.CommentService;
import com.api.instaclone.service.PostImageService;
import com.api.instaclone.service.UserInfoService;


@Repository
public class PostRepository {

    // PostRepositoryConstants postRepositoryConstants;
    @Value("${myapp.deployment.backend.sql}")
    private String JdbcURL;
    
    private String username = "root";
    private String password = "qwerty11";

    @Autowired
    CommentService commentService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired 
    ActionService actionService;

    @Autowired
    PostImageService postImageService;

    Connection connection = null;

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }

    public List<Post> getAllPosts(){
        List<Post> posts = new ArrayList<>();

        String sql = "SELECT * FROM posts";
        String userinfo_query= "SELECT userid,username,profile_image FROM userinfo WHERE userid=?";

        try(Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(sql);
            PreparedStatement preparedStatementForUserinfo = connect().prepareStatement(userinfo_query);
            ResultSet resultSet = preparedStatement.executeQuery();
            ){  
                Userinfo ownerUserinfo = null;
                while (resultSet.next()) {
                    int id = resultSet.getInt("post_id");
                    int owner_id = resultSet.getInt("owner_id");
                    String description = resultSet.getString("description");
                   
                    preparedStatementForUserinfo.setInt(1, owner_id);
                    ResultSet resultSetForUserinfo = preparedStatementForUserinfo.executeQuery();
                    

                    //this adds user data
                    while (resultSetForUserinfo.next()){
                        int userid = resultSetForUserinfo.getInt("userid");
                        String username = resultSetForUserinfo.getString("username");
                        String profileimage = resultSetForUserinfo.getString("profile_image");

                        ownerUserinfo = new Userinfo(userid,username,profileimage);

                    }
                        
                  
                    Post post = new Post(id,owner_id,description,ownerUserinfo);
                    posts.add(post);
                }

            connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        return posts;
    }

    public List<Post> getFollowingPosts(int id){
        String sql = "SELECT * FROM posts WHERE owner_id IN (SELECT following_id FROM followers WHERE followers.usr_id=?)";
        String userinfo_query= "SELECT userid,username,profile_image FROM userinfo WHERE userid=?";
        String listAllLikes= "SELECT * FROM action WHERE action=? and post_id=?";
        
       
        List<Post> posts = new ArrayList<>();
        // List<PostImage> postImages = new ArrayList<>();
            
        System.out.println("called");       
        try(Connection connection = connect();
            
            PreparedStatement preparedStatement = connect().prepareStatement(sql);
            PreparedStatement preparedStatementForUserinfo = connect().prepareStatement(userinfo_query);
            PreparedStatement preparedStatementForLikes=connect().prepareStatement(listAllLikes);
            ){  
                System.out.println("executed");
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                Userinfo ownerUserinfo = null;
                    
                while (resultSet.next()) {
                    int post_id = resultSet.getInt("post_id");
                    int owner_id = resultSet.getInt("owner_id");
                    String description = resultSet.getString("description");
                    Boolean has_liked=false;
                    List<Action> likes=new ArrayList<>();
                    preparedStatementForUserinfo.setInt(1, owner_id);
                    ResultSet resultSetForUserinfo = preparedStatementForUserinfo.executeQuery();
                    preparedStatementForLikes.setString(1, "like");
                    preparedStatementForLikes.setInt(2, post_id);
                    PostImage image = postImageService.getPostImageByPostId(post_id);
                    System.out.printf("\nGOt post image in following %d %s",image.getPost_id(),image.getImageName());
                
                    ResultSet resultSetForLikes = preparedStatementForLikes.executeQuery();
                    List<Comment> comments=commentService.getCommentByPost(post_id);                
                    while (resultSetForLikes.next()){
                        
                        int user_id_likes=resultSetForLikes.getInt("user_id");
                        System.out.println("executed");
                        int post_id_likes = resultSetForLikes.getInt("post_id");
                        String action = resultSetForLikes.getString("action");
                        System.out.println("Executedlikes detail, user id ");
                        System.out.println(user_id_likes);
                        if (id==user_id_likes){
                            System.out.print("liked");
                            has_liked=true;
                        }

                        Action LikedPost=new Action(post_id_likes,user_id_likes,action);
                        likes.add(LikedPost);
                        
                    }

                    resultSetForLikes.close();
                    while (resultSetForUserinfo.next()){
                        int userid = resultSetForUserinfo.getInt("userid");
                        String username = resultSetForUserinfo.getString("username");
                        String profileimage = resultSetForUserinfo.getString("profile_image");

                        ownerUserinfo = new Userinfo(userid,username,profileimage);

                    }
                    resultSetForUserinfo.close();
                    Post post = new Post(post_id,owner_id,image,description,ownerUserinfo,has_liked,likes,comments);

                    posts.add(post);
                }
            connection.close();   
            }catch(SQLException e){
                e.printStackTrace();
            }
        return posts;
    }

    public List<Post> getHomePagePosts(int userProfileId,int visitorId){
        List<Post> posts= new ArrayList<>();
        String sql = "SELECT posts.*, COUNT(action.action) as action_count FROM posts LEFT JOIN action ON posts.post_id = action.post_id WHERE owner_id=? GROUP BY posts.post_id;";
        System.out.println("called home posts");       
        try(Connection connection = connect();
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ){  
                preparedStatement.setInt(1, userProfileId);
                ResultSet resultSet = preparedStatement.executeQuery();
            
                while (resultSet.next()) {
                    int post_id = resultSet.getInt("post_id");
                    int owner_id = resultSet.getInt("owner_id");
                    String description = resultSet.getString("description");
                    Userinfo ownerUserinfo = userInfoService.getUserinfo(owner_id);
                    List<Action> likes = actionService.getActionByPostId(post_id);
                    Boolean hasLiked= actionService.userHasLikedPost(visitorId, post_id);
                    List<Comment> comments = commentService.getCommentByPost(post_id);
                    PostImage image = postImageService.getPostImageByPostId(post_id);
                    // Integer likes= resultSet.getInt("action_count");
                    Post post = new Post(post_id,owner_id,image,description,ownerUserinfo,hasLiked,likes,comments);
                    posts.add(post);
                    }
            connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        return posts;
    }

    public void addPost(Post post,String imageName){

        
        String insertPostDataSql = "INSERT INTO posts(owner_id,description) VALUES(?,?)";
        System.err.printf("%d got new post",post.getOwner_id());
        // String getLastPostId="LAST_INSERT_ID();";x``
        String insertImageDataSql = "INSERT INTO post_images(post_id,image_name) VALUES(?,?)";
        try  
            {
            Connection connection = connect();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatementForPost = connection.prepareStatement(insertPostDataSql,Statement.RETURN_GENERATED_KEYS);
            preparedStatementForPost.setInt(1, post.getOwner_id());
            preparedStatementForPost.setString(2, post.getDescription());
            preparedStatementForPost.executeUpdate();
            
            // PreparedStatement preparedStatementToGetPostId=connection.prepareStatement(getLastPostId);
            ResultSet generatedKeys=preparedStatementForPost.getGeneratedKeys();

            
            int postId = 0;
            // ResultSet generatedKeys = preparedStatementToGetPostId;
            if (generatedKeys.next()) {
                postId = generatedKeys.getInt(1); // The generated post_id
            } else {
                throw new SQLException("Failed to retrieve post_id.");
            }

            
            PreparedStatement preparedStatementForImages = connection.prepareStatement(insertImageDataSql);
            preparedStatementForImages.setInt(1, postId);
            preparedStatementForImages.setString(2, imageName);
            preparedStatementForImages.executeUpdate();
            connection.commit();
            
        connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
      
    }

    public List<Post> getPostsBySearchingDescripition(String searchStr){
        String sql= "SELECT * FROM posts WHERE description LIKE ?";
        List<Post> posts=new ArrayList<Post>();
        
        
        try{
            Connection connection = connect();

            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1, "%"+searchStr+"%");

            ResultSet resultSet= preparedStatement.executeQuery();

            while(resultSet.next()){
                int post_id=resultSet.getInt("post_id");
                int owner_id=resultSet.getInt("owner_id");
                PostImage image=postImageService.getPostImageByPostId(post_id);
                Userinfo userinfo=userInfoService.getUserinfo(owner_id);    

                
                Post post=new Post(post_id,userinfo,image);                
                
                posts.add(post);
            }
        connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return posts;
    }

    public void deletePost(int post_id){

        try{
            Connection connection=connect();
            String sql="DELETE FROM posts WHERE post_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, post_id);

            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Post getPostById(int cur_post_id){

        String sql = "SELECT * FROM posts WHERE post_id=?";

        try{
            
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cur_post_id);
            
            ResultSet resultSet = preparedStatement.executeQuery();if (resultSet.next()) {
                int post_id = resultSet.getInt("post_id");
                int owner_id = resultSet.getInt("owner_id");
                String description = resultSet.getString("description");
                Userinfo userinfo = userInfoService.getUserinfo(owner_id);
                
                return new Post(post_id, owner_id, description, userinfo);
            }
        connection.close();
        }catch( SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}