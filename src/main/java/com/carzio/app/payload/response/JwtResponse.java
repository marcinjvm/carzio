package com.carzio.app.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private final List<String> roles;
    private String token;
    private String id;
    private String username;

    public JwtResponse(String accessToken, String id, String username, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public void setId(String id) {
        this.id = id;
    }
}
