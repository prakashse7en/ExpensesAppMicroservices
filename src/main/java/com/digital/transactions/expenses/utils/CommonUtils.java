package com.digital.transactions.expenses.utils;

import com.digital.transactions.expenses.pojo.model.User;

import java.util.UUID;

public class CommonUtils {

    public static void optimizeUserNameForDefaultUser(UUID userId, User user) {
        if(user.getUserName().equalsIgnoreCase("DEFAULT_USER")){
            user.setUserName(user.getUserName()+ userId);
        }
    }
}
