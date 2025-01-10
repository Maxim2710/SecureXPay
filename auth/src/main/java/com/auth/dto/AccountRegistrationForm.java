package com.auth.dto;

import lombok.Data;

@Data
public class AccountRegistrationForm {
    private String username;
    private String email;
    private String password;
}
