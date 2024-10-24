package com.api.instaclone.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

// import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            String header = request.getHeader("Authorization");

            if (header ==null || !header.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }
        
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(); 
        String token = header.replaceAll("Bearer ", "");
        var roles= JWT.decode(token).getClaim("roles").asList(String.class);
        authorities.addAll(roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));


        String userName = JWT.require(Algorithm.HMAC512("bQeThWmZq4t7w!z$C&F)J@NcRfUjXn2r5u8x/A?D*G-KaPdSgVkYp3s6v9y$B&E)"))
                            .build()
                            .verify(token)
                            .getSubject();
        System.out.println("JWT token passed username"+userName);
        
        for (var authority:authorities){
            System.out.println(authority.getAuthority().toString());
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(userName, null,authorities);
        System.out.println("check "+authentication.isAuthenticated());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("check "+SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
        filterChain.doFilter(request, response);
        return;
    }
}
