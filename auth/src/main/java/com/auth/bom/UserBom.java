package com.auth.bom;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserBom {
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
