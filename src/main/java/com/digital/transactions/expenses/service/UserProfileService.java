package com.digital.transactions.expenses.service;

import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.pojo.model.User;

import java.util.UUID;

public interface UserProfileService {

    User getUserProfileByUserId(UUID userId) throws UserNotFoundException;
    void evictUserProfileCache(UUID userId);

}
