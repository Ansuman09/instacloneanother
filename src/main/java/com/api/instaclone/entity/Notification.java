package com.api.instaclone.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification implements Serializable {
    
    private int id;           // Declare the fields at the class level
    private String uname;
    private String action;
    private int count;
    private String actinguser;
    private String status;
    private String message;
    private int post_id;      

   
    public Notification(String uname, String action, int count, String actinguser, int post_id, String message) {
        this.uname = uname;
        this.action = action;
        this.count = count;
        this.actinguser = actinguser;
        this.post_id = post_id;  
        this.message = message;
    }

    public Notification(String uname,String action,int count,String actinguser){
        this.uname=uname;
        this.count = count;
        this.action= action;
        this.actinguser = actinguser;
    }

}
