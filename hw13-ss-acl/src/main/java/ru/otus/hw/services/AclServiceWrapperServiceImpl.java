package ru.otus.hw.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private static final List<Permission> ALL_PERMISSIONS = List.of(
        BasePermission.READ,
        BasePermission.WRITE,
        BasePermission.CREATE,
        BasePermission.DELETE,
        BasePermission.ADMINISTRATION
    );

    private final MutableAclService mutableAclService;

    public AclServiceWrapperServiceImpl(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN', 'ROLE_USER_EDITOR')")
    public void createPermissions(Object object, List<Permission> permissions) {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        MutableAcl acl = mutableAclService.createAcl(oid);

        Sid adminSid = new GrantedAuthoritySid("ROLE_SUPER_ADMIN");
        Sid editorSid = new GrantedAuthoritySid("ROLE_USER_EDITOR");
        Sid observerSid = new GrantedAuthoritySid("ROLE_USER_OBSERVER");

        for (Permission p : permissions) {
            acl.insertAce(acl.getEntries().size(), p, editorSid, true);
        }
        for (Permission p : ALL_PERMISSIONS) {
            acl.insertAce(acl.getEntries().size(), p, adminSid, true);
        }
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, observerSid, true);

        mutableAclService.updateAcl(acl);
    }
}
