package com.hieu.service.impl;

import com.hieu.model.Role;
import com.hieu.model.RoleName;
import com.hieu.repository.IRoleRepository;
import com.hieu.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    IRoleRepository roleRepository;
    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}

