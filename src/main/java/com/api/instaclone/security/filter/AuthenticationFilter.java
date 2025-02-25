package com.api.instaclone.security.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.api.instaclone.entity.User;
import com.api.instaclone.security.manager.CustomAuthenticationManager;
import com.api.instaclone.service.RoleService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
// import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
// import lombok.experimental.Accessors;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    @Autowired
    CustomAuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        try {User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            System.out.println("--filter---"+user.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return authenticationManager.authenticate(authentication);
    } catch (IOException e){
            throw new RuntimeException();
    }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        Set<GrantedAuthority> authorities= roleService.getRoleFromUsername(authResult.getPrincipal().toString());
        String token = JWT.create()
                        .withSubject(authResult.getPrincipal().toString())
                        .withExpiresAt(new Date(System.currentTimeMillis()+7200000))
                        .withClaim("roles",authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(Algorithm.HMAC512("bQeThWmZq4t7w!z$C&F)J@NcRfUjXn2r5u8x/A?D*G-KaPdSgVkYp3s6v9y$B&E)"));        
        for (var authority:authorities){
            System.out.println("Authoruty check at creation");
            System.out.println(authority.getAuthority().toString());
        }
        response.addHeader("Authorization","Bearer "+token);
        response.addHeader("Access-Control-Expose-Headers","Authorization");
        System.out.println("Successfully authenticated");
        
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        // TODO Auto-generated method stub
            System.out.println("Not authenticated");
    }
}
