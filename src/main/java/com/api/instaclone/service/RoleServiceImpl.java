package com.api.instaclone.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.api.instaclone.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Set<GrantedAuthority> getRoleFromUsername(String username) {
        return roleRepository.getRoleByUsername(username);
    }
}
