package com.trellis.commondata.dto;

import lombok.Data;

@Data
public class AccessRightsResponse {
    private String csrfToken;
    private String name;
    private String username;
    private boolean isAdmin;
}
