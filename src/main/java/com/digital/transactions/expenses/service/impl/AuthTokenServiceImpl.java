package com.digital.transactions.expenses.service.impl;

import com.digital.transactions.expenses.pojo.model.User;
import com.digital.transactions.expenses.service.AuthTokenService;
import com.digital.transactions.expenses.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;


@Component
public class AuthTokenServiceImpl implements AuthTokenService {

    static final String GRANT_TYPE_CLIENT_CREDENTIALS = "password";
    static final String CLIENT_ID = "expenses-clientid";
    static final String PASSWORD = "password";
    static final String USERNAME = "expensesuser";
    static final String TOKEN_ENDPOINT = "http://localhost:8080/realms/expenses/protocol/openid-connect/token";

    @Autowired
    RestTemplate restTemplate;


    @Override
    public String getToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("username", USERNAME);
        map.add("password", PASSWORD);
        map.add("client_id", CLIENT_ID);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_ENDPOINT, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse the JSON response to extract the access token
                // (You'll likely want to use a JSON library like Jackson for this)
                String responseBody = response.getBody();


                // Basic example (not recommended for production, use a JSON library):
                if (responseBody != null && responseBody.contains("access_token")) {
                    int startIndex = responseBody.indexOf("access_token") + "access_token".length() + 3; // ":\"".length()
                    int endIndex = responseBody.indexOf("\"", startIndex);
                    if (endIndex > startIndex) {
                        return responseBody.substring(startIndex, endIndex);
                    }
                }

                return null; // Or throw an exception if access_token is not found
            } else {
                System.err.println("Error: Received status code " + response.getStatusCode());
                // Handle error cases appropriately (e.g., throw an exception)
                return null;
            }
        } catch (Exception e) {
            System.err.println("Exception while calling token endpoint: " + e.getMessage());
            // Handle exceptions appropriately (e.g., throw an exception)
            return null;
        }
    }


}
