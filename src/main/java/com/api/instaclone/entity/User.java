package com.api.instaclone.entity;

// import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {
    
    int usr_id ;
    String username;
    String password;
    String email;
    
    public User(String username, String password){
        this.username=username;
        this.password=password;
    }
    
    public User(int id,String username, String password){
        this.usr_id=id;
        this.username=username;
        this.password=password;
    }
    
    public User(String email,String username, String password){
        this.email=email;
        this.username=username;
        this.password=password;
    }

    public User(int id){
        this.usr_id=id;
    }
}
