package com.auth.converter;

import com.auth.bom.UserBom;
import com.auth.model.user.User;

public class UserConverter {

    public static UserBom convertToBom(User user) {
        UserBom bom = new UserBom();
        bom.setUsername(user.getUsername());
        bom.setEmail(user.getEmail());
        bom.setCreatedAt(user.getCreatedAt());
        bom.setUpdatedAt(user.getUpdatedAt());

        return bom;
    }
}
