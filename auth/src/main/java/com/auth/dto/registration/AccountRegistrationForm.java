package com.auth.dto.registration;

import lombok.Data;

@Data
public class AccountRegistrationForm {
    private String username;
    private String email;
    private String password;
}
