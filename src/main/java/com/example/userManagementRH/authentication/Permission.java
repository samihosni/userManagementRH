package com.example.userManagementRH.authentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_WRITE("employee:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    HR_READ("hr:read"),
    HR_WRITE("hr:write");

    private final String permission;



    public String getPermission() {
        return permission;
    }
}
