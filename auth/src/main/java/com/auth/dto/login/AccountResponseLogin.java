package com.auth.dto.login;

import com.auth.bom.UserBom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseLogin {
    private String message;
    private String token;
    private UserBom user;
}
