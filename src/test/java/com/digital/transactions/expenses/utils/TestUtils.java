// src/test/java/com/digital/transactions/expenses/utils/TestUtils.java
package com.digital.transactions.expenses.utils;

import io.restassured.response.Response;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;

import static io.restassured.RestAssured.given;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

public class TestUtils {



     public static Response getPostResponse(String token, String userId) {
        String bodyPayload = String.format(
                """
                {
                     "userId": "%s",
                     "expenseAmount": 100.73,
                     "category": "Travel",
                     "description": "Random expense 398"
                }
                """, userId);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(bodyPayload)
                .when()
                .post("/api/v1/expenses");
        return response;
    }
}