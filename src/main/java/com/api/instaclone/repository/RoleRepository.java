package com.api.instaclone.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.api.instaclone.entity.Role;
import com.api.instaclone.entity.User;
import com.api.instaclone.service.UserService;

@Repository
public class RoleRepository {
    
    @Value("${myapp.deployment.backend.sql}")
    private String JdbcURL;
    
    private String username = "root";
    private String password = "qwerty11";

    Connection connection = null;

    @Autowired
    UserService userService;

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(this.JdbcURL, this.username, this.password);
    }

    public Set<GrantedAuthority> getRoleByUsername(String username){
        String sql="SELECT * FROM roles WHERE username=?";
        Set<GrantedAuthority> authorities= new HashSet<>();
        Set<Role> roles= new HashSet<>();
        try(Connection connection = connect();){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                Role role=new Role();
                role.setUsername(resultSet.getString("username"));
                role.setRole("ROLE_"+resultSet.getString("role").toUpperCase());
                // System.out.println("db roles:: " + role.getRole());
                roles.add(role);
            }
        connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }

        authorities=roles.stream().map(role->new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toSet());

    return authorities;
    }    

    public void addRole(User user){
        String sql = "INSERT INTO roles (username,role) values(?,?)";

        try(Connection connection=connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,"RW_USER");

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    
    public void editRole(User user,String role){
        String sql = " UPDATE roles SET role=? WHERE username=?;";

        try(Connection connection=connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(2,user.getUsername());
            preparedStatement.setString(1,role);

            preparedStatement.executeUpdate();
            System.out.printf("Changed user %s role to %s",user.getUsername(),role);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
