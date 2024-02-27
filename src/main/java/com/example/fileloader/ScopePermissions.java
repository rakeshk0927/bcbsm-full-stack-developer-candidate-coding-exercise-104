package com.example.fileloader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ScopePermissions {
    // Define your request URI patterns and associated scopes
    READ_DATA("/api/data/read", "read:data"),
    WRITE_DATA("/api/data/write", "write:data"),
    READ_PROFILE("/api/profile/read", "read:profile"),
    WRITE_PROFILE("/api/profile/write", "write:profile");

    private final String requestURI;
    private final Set<String> allowedScopes;

    ScopePermissions(String requestURI, String... allowedScopes) {
        this.requestURI = requestURI;
        this.allowedScopes = new HashSet<>(Arrays.asList(allowedScopes));
    }

    public String getRequestURI() {
        return requestURI;
    }

    public Set<String> getAllowedScopes() {
        return allowedScopes;
    }

    public static boolean hasPermission(String requestURI, Set<String> tokenScopes) {
        for (ScopePermissions scopePermission : ScopePermissions.values()) {
            if (requestURI.matches(scopePermission.getRequestURI())) {
                return tokenScopes.containsAll(scopePermission.getAllowedScopes());
            }
        }
        return false; // Default to deny if no matching request URI found
    }
}