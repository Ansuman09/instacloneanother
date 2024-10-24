package com.api.instaclone.service;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    
    public Set<GrantedAuthority> getRoleFromUsername(String username);
}
