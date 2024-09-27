package com.api.instaclone.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostImage {
    
    String imageName;
    int post_id;

    public PostImage(int post_id, String imageName){
        this.post_id=post_id;
        this.imageName=imageName;
    }
}
