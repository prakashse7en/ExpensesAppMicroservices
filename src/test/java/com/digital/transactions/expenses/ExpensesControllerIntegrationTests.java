package com.digital.transactions.expenses;

import com.digital.transactions.expenses.pojo.model.Error;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;
import com.digital.transactions.expenses.repository.ExpensesRepository;
import com.digital.transactions.expenses.service.impl.UserProfileServiceImpl;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static com.digital.transactions.expenses.utils.TestUtils.*;
import static io.restassured.RestAssured.given;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(ContainersConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpensesControllerIntegrationTests {

    static final String GRANT_TYPE_CLIENT_CREDENTIALS = "password";
    static final String CLIENT_ID = "expensesclientadmin"; //TODO JWT change
    static final String PASSWORD = "password";
    static final String USERNAME = "expensesuser";

    @LocalServerPort
    private int port;

    @Autowired
    OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Autowired
    ExpensesRepository expensesRepository;

    @Autowired
    private UserProfileServiceImpl userProfileService;


    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.30")
            .withDatabaseName("testcontainer")
            .withUsername("test")
            .withPassword("test");

    static MockServerContainer mockServerContainer = new MockServerContainer(
            DockerImageName.parse("mockserver/mockserver:5.15.0")
    );

    static MockServerClient mockServerClient;



    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
        mockServerContainer.start();
    }


    @BeforeEach
    void setup() {
        RestAssured.port = port;
        mockServerClient.reset();

    }


    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        mockServerClient =
                new MockServerClient(
                        mockServerContainer.getHost(),
                        mockServerContainer.getServerPort()
                );
        registry.add("userprofile.service.url", () -> mockServerContainer.getEndpoint() );

    }

    @Test
    @Order(1)
    @DisplayName("Return unauthorized when creating expenses with invalid auth token")
    void shouldGetUnauthorizedWhenCreateGetUserWithoutAuthToken() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + "invalid_token")
                .when()
                .post("/api/v1/expenses")
                .then()
                .statusCode(401);
    }


    @Test
    @Order(2)
    @DisplayName("Return success when sending valid auth token , user sent is found and expense is created")
    void createExpensesWithValidAuthToken() {
        String token = getToken();
        ReflectionTestUtils.setField(userProfileService, "userprofileEndpoint",
                mockServerContainer.getEndpoint() + "/api/user/");
        mockServerClient
                .when(
                        request().withMethod("GET")
                                .withPath("/api/user/ff95ae18-b0df-4e35-b08b-246139cfeb46")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(new Header("Content-Type", "application/json"))
                                .withBody(
                                        json(
                                                """
                                                {  
                                                   "userId": "ff95ae18-b0df-4e35-b08b-246139cfeb46",
                                                   "userName": "seconduserupdated3",
                                                   "userPhoneNumber": "+85265783645",
                                                   "userEmail": "sample@add.com",
                                                   "recordCreatedDateTime": null,
                                                   "recordUpdatedDateTime": "2025-08-11T03:19:49Z"
                                                }
                                                """
                                        )
                                )
                );
        Response response = getPostResponse(token,"ff95ae18-b0df-4e35-b08b-246139cfeb46");

        ExpensesDto expensesDto = response.as(ExpensesDto.class);
        assertNotNull(expensesDto);
        expensesRepository.findById(expensesDto.getExpenseId())
                .ifPresent(expense -> {
                    assertNotNull(expense.getExpenseId());
                    assertNotNull(expense.getUserId());
                    assertNotNull(expense.getCategory());
                    assertNotNull(expense.getDescription());
                    assertNotNull(expense.getExpenseAmount());
                });

    }

    @Test
    @Order(3)
    @DisplayName("Return user not found when sending valid auth token and invalid user")
    void failExpensesCreationWithAndInvalidUser() {
        // Get a valid token for authentication.
        String token = getToken();
        ReflectionTestUtils.setField(userProfileService, "userprofileEndpoint",
                mockServerContainer.getEndpoint() + "/api/user/");
        // Set the mock server client to return a user not found response.

            mockServerClient
                    .when(
                            request().withMethod("GET")
                                    .withPath("/api/user/"+"963adf26-7c0b-4b0e-80dc-882464dd8b18")
                    )
                    .respond(
                            response()
                                    .withStatusCode(404)
                                    .withHeaders(new Header("Content-Type", "application/json"))
                                    .withBody(
                                            json(
                                                    """
                                                    {
                                                        "message": "User Not found.",
                                                        "code": "BG2000"
                                                    }
                                                    """
                                            )
                                    )

                    );

        // Send POST request and capture response.
        Response response = getPostResponse(token,"963adf26-7c0b-4b0e-80dc-882464dd8b18");
        System.out.println("Response: " + response.asString());
        // Convert response to Error class.
        Error error = response.as(Error.class);

        // Verify error object contains expected details.
        assertNotNull(error);
        assertEquals("BG1002", error.getError());
    }

    private String getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("grant_type", singletonList(GRANT_TYPE_CLIENT_CREDENTIALS));
        map.put("client_id", singletonList(CLIENT_ID));
        map.put("username", singletonList(USERNAME));
        map.put("password", singletonList(PASSWORD));
//TODO change it to working openid token endpoint
        String authServerUrl ="http://localhost:8080/realms/expenses/protocol/openid-connect/token";
     /* oAuth2ResourceServerProperties.getJwt().getIssuerUri() +
      "/protocol/openid-connect/token";*/

        var request = new HttpEntity<>(map, httpHeaders);
        ExpensesControllerIntegrationTests.KeyCloakToken token = restTemplate.postForObject(
                authServerUrl,
                request,
                ExpensesControllerIntegrationTests.KeyCloakToken.class
        );

        assert token != null;
        return token.accessToken();
    }

    record KeyCloakToken(@JsonProperty("access_token") String accessToken) {}

}
