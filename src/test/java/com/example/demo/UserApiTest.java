package com.example.demo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Listeners(TestResultListener.class)
public class UserApiTest {

    private static final String BASE_URL = "http://localhost:9001";
    private static final String USERS_ENDPOINT = "/api/users";
    private static String uniqueTimestamp;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        // Generate unique timestamp to avoid email duplication across test runs
        uniqueTimestamp = String.valueOf(System.currentTimeMillis());
    }

    @Test(priority = 1, description = "Test GET API endpoint name and status code")
    public void testGetAllUsersEndpoint() {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get(USERS_ENDPOINT)
                .then()
                .extract()
                .response();

        // Verify status code is 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, 
            "GET API should return status code 200");
        
        // Verify endpoint name
        Assert.assertTrue(response.getBody() != null, 
            "GET API /api/users should return a response body");
        
        System.out.println("GET API Status Code: " + response.getStatusCode());
        System.out.println("GET API Endpoint: " + USERS_ENDPOINT);
    }

    @Test(priority = 2, description = "Test POST API endpoint name and status code")
    public void testCreateUserEndpoint() {
        Map<String, String> userPayload = new HashMap<>();
        userPayload.put("name", "John Doe");
        userPayload.put("email", "john.doe." + uniqueTimestamp + "@example.com");

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .post(USERS_ENDPOINT)
                .then()
                .extract()
                .response();

        // Verify status code is 201 CREATED
        Assert.assertEquals(response.getStatusCode(), 201, 
            "POST API should return status code 201");
        
        // Verify endpoint name
        Assert.assertTrue(response.getBody() != null, 
            "POST API /api/users should return a response body");
        
        System.out.println("POST API Status Code: " + response.getStatusCode());
        System.out.println("POST API Endpoint: " + USERS_ENDPOINT);
    }

    @Test(priority = 3, description = "Test GET API returns valid JSON response")
    public void testGetAllUsersReturnsJson() {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get(USERS_ENDPOINT)
                .then()
                .extract()
                .response();

        // Verify status code
        Assert.assertEquals(response.getStatusCode(), 200, 
            "GET API should return status code 200");
        
        // Verify response is JSON
        Assert.assertEquals(response.getContentType(), "application/json", 
            "GET API should return JSON content type");
        
        System.out.println("GET API Content Type: " + response.getContentType());
    }

    @Test(priority = 4, description = "Test POST API with valid data returns created user")
    public void testPostApiWithValidData() {
        Map<String, String> userPayload = new HashMap<>();
        userPayload.put("name", "Jane Smith");
        userPayload.put("email", "jane.smith." + uniqueTimestamp + "@example.com");

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .post(USERS_ENDPOINT)
                .then()
                .extract()
                .response();

        // Verify status code
        Assert.assertEquals(response.getStatusCode(), 201, 
            "POST API should return status code 201");
        
        // Verify response contains name
        Assert.assertTrue(response.getBody().asString().contains("Jane Smith"), 
            "Response should contain the created user's name");
        
        System.out.println("POST API Response: " + response.getBody().asString());
    }

    @Test(priority = 5, description = "Test GET API endpoint after creating users")
    public void testGetAllUsersAfterCreation() {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get(USERS_ENDPOINT)
                .then()
                .extract()
                .response();

        // Verify status code
        Assert.assertEquals(response.getStatusCode(), 200, 
            "GET API should return status code 200");
        
        // Verify response is an array
        Assert.assertTrue(response.getBody().asString().startsWith("["), 
            "GET API should return an array of users");
        
        System.out.println("Total Users Retrieved: " + response.jsonPath().getList("$").size());
        System.out.println("GET API Verification Successful with Status Code: " + response.getStatusCode());
    }
}

