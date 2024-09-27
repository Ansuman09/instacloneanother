package com.api.instaclone.security.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.api.instaclone.entity.User;
// import com.api.instaclone.repository.UserRepository;
import com.api.instaclone.service.UserService;

// import lombok.extern.apachecommons.CommonsLog;

@Component
public class CustomAuthenticationManager implements AuthenticationManager{
    
    @Autowired
    private UserService userService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userService.getUser(authentication.getPrincipal().toString());
        System.out.println("\n--------------------"+authentication.getPrincipal());
        if (!user.getPassword().equals(authentication.getCredentials().toString())){
            throw new BadCredentialsException("You have provided wrong pass");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal().toString(),authentication.getCredentials().toString());
    }
}
