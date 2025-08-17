package com.digital.transactions.expenses.service.impl;

import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.pojo.model.User;
import com.digital.transactions.expenses.service.AuthTokenService;
import com.digital.transactions.expenses.service.UserProfileService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AuthTokenService authTokenService;

    private static final String USERPROFILE_SERVICE = "userprofileService";
    private final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Value("${userprofile.service.url}")
    private String userprofileEndpoint;

    @Override
    @Cacheable(value = "userprofileCache", key = "#userId")
    //@CircuitBreaker(name = USERPROFILE_SERVICE, fallbackMethod = "fallbackUserprofileResponse")
    public User getUserProfileByUserId(final UUID userId) throws UserNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+authTokenService.getToken()); // Set the Authorization header
        headers.setContentType(MediaType.APPLICATION_JSON); // Assuming the API returns JSON

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<User> responseEntity = restTemplate.exchange(
                    userprofileEndpoint +userId, // Construct the full URL
                    HttpMethod.GET,
                    requestEntity,
                    User.class // Map the response to the User class
            );

           return responseEntity.getStatusCode() == HttpStatus.OK ? responseEntity.getBody()
                    : null;
        }catch (HttpClientErrorException.NotFound ex) {

            logger.error("User profile not found for userId: {}", userId);
            throw new UserNotFoundException("User not found with ID: " + userId);

        }
    }

    public User fallbackUserprofileResponse(Throwable t) {
        User user = new User();
        user.setUserName("DEFAULTUSER");
        return user;
    }

    @CacheEvict(value = "userprofileCache", key = "#userId")
    public void evictUserProfileCache(UUID userId) {
        // This method will evict all entries from the userprofileCache
       logger.debug("Evicting user profile cache for userId: {}", userId);
       //additional logic can be added here if needed
    }


}
