package com.hieu.service;

import com.hieu.model.Role;
import com.hieu.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
