package com.api.instaclone.service;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.api.instaclone.entity.User;

@Service
public interface RoleService {
    
    public Set<GrantedAuthority> getRoleFromUsername(String username);
    public void addRole(User user);
    public void updateRole(User user,String role);
}
