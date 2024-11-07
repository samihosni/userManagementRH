package com.example.userManagementRH.entities;

import com.example.userManagementRH.authentication.Permission;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ERole {

    EMPLOYEE(Set.of(Permission.EMPLOYEE_READ, Permission.EMPLOYEE_WRITE)),
    ADMIN(Set.of(Permission.ADMIN_READ, Permission.ADMIN_WRITE)),
    HR(Set.of(Permission.HR_READ, Permission.HR_WRITE));

    private final Set<Permission> permissions;

    ERole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
