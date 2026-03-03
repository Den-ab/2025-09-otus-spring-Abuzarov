package ru.otus.hw.services;

import org.springframework.security.acls.model.Permission;

import java.util.List;

public interface AclServiceWrapperService {

    void createPermissions(Object object, List<Permission> permissions);
}
