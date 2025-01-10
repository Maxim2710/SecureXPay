package com.auth.dto;

import com.auth.bom.UserBom;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountResponseRegister {
    private String message;
    private String token;
    private UserBom user;
}
