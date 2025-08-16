package com.digital.transactions.expenses.service.impl;

import com.digital.transactions.expenses.service.AuthTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class AuthTokenServiceImpl implements AuthTokenService {


    @Value("${jwt.credts.clientId}")
    private String clientId;
    @Value("${jwt.credts.password}")
    private String password;
    @Value("${jwt.credts.username}")
    private String userName;
    @Value("${jwt.credts.url}")
    private String tokenEndpoint;



    @Autowired
    RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(AuthTokenServiceImpl.class);



    @Override
    public String getToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("username", userName);
        map.add("password", password);
        map.add("client_id", clientId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                if (responseBody != null && responseBody.contains("access_token")) {
                    int startIndex = responseBody.indexOf("access_token") + "access_token".length() + 3; // ":\"".length()
                    int endIndex = responseBody.indexOf("\"", startIndex);
                    if (endIndex > startIndex) {
                        return responseBody.substring(startIndex, endIndex);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception while calling token endpoint", e);
            throw e;
        }
        return null;
    }

}
