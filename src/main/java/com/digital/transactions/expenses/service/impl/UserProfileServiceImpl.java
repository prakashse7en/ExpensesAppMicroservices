package com.digital.transactions.expenses.service.impl;

import com.digital.transactions.expenses.pojo.model.User;
import com.digital.transactions.expenses.service.AuthTokenService;
import com.digital.transactions.expenses.service.UserProfileService;
import com.digital.transactions.expenses.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AuthTokenService authTokenService;



    //cache the user profile by userId
    @Override
    public User getUserProfileByUserId(UUID userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+authTokenService.getToken()); // Set the Authorization header
        headers.setContentType(MediaType.APPLICATION_JSON); // Assuming the API returns JSON

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<User> responseEntity = restTemplate.exchange(
                    Constants.API_URL +userId, // Construct the full URL
                    HttpMethod.GET,
                    requestEntity,
                    User.class // Map the response to the User class
            );

           return responseEntity.getStatusCode() == HttpStatus.OK
                    ? responseEntity.getBody()
                    : null;
        } catch (Exception e) {
            // Handle exceptions (e.g., network issues, invalid URL)
            System.err.println("Exception while calling API: " + e.getMessage());
            return null; // Or throw an exception
        }
    }


}
