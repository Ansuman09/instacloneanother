package com.api.instaclone.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Role {
    private String username;
    private String role;
    
    public Role(String username,String role){
        this.username=username;
        this.role=role;
    }

}
