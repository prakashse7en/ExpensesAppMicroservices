package com.digital.transactions.expenses.aop;

import com.digital.transactions.expenses.pojo.model.Error;
import com.digital.transactions.expenses.pojo.model.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthorizationExceptionAdvice implements AuthenticationEntryPoint {

    @Autowired
    private final ObjectMapper objectMapper;

    public AuthorizationExceptionAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set 401 status
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Set content type to JSON
        Error errorResponse = Error.fromErrorCode(ErrorCode.BG1001);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse); // Serialize to JSON
        response.getWriter().write(jsonResponse);

    }


}
